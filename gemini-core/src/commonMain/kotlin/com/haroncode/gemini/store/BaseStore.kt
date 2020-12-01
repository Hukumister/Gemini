package com.haroncode.gemini.store

import com.haroncode.gemini.element.Bootstrapper
import com.haroncode.gemini.element.ErrorHandler
import com.haroncode.gemini.element.EventProducer
import com.haroncode.gemini.element.Middleware
import com.haroncode.gemini.element.Reducer
import com.haroncode.gemini.element.Store
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

/**
 * @author HaronCode
 * @author kdk96
 */
open class BaseStore<Action : Any, State : Any, Event : Any, Effect : Any>(
    initialState: State,
    reducer: Reducer<State, Effect>,
    middleware: Middleware<Action, State, Effect>,
    bootstrapper: Bootstrapper<Action>? = null,
    eventProducer: EventProducer<State, Effect, Event>? = null,
    errorHandler: ErrorHandler<State>? = null,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : Store<Action, State, Event> {

    final override val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + coroutineDispatcher)

    private val _stateFlow = MutableStateFlow(initialState)
    private val actionFlow = MutableSharedFlow<Action>()
    private val _eventFlow = MutableSharedFlow<Event>()

    final override val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    final override val eventFlow: Flow<Event> = _eventFlow.asSharedFlow()

    init {
        val reducerWrapper: Reducer<State, Effect>
        val middlewareWrapper: Middleware<Action, State, Effect>
        val bootstrapperWrapper: Bootstrapper<Action>?
        val eventProducerWrapper: EventProducer<State, Effect, Event>?

        if (errorHandler == null) {
            reducerWrapper = reducer
            middlewareWrapper = middleware
            bootstrapperWrapper = bootstrapper
            eventProducerWrapper = eventProducer
        } else {
            reducerWrapper = ReducerWrapper(reducer, errorHandler)
            middlewareWrapper = MiddlewareWrapper(middleware, errorHandler)
            bootstrapperWrapper = bootstrapper?.let {
                BootstrapperWrapper(it, errorHandler, initialState)
            }
            eventProducerWrapper = eventProducer?.let {
                EventProducerWrapper(it, errorHandler)
            }
        }

        val effectFlow = merge(
            actionFlow,
            bootstrapperWrapper?.bootstrap() ?: emptyFlow()
        )
            .flatMapMerge { action -> middlewareWrapper.execute(action, _stateFlow.value) }
            .shareIn(coroutineScope, SharingStarted.WhileSubscribed(), replay = 1)

        val stateFlow = effectFlow
            .scan(initialState, reducerWrapper::reduce)
            .shareIn(coroutineScope, SharingStarted.WhileSubscribed(), replay = 1)

        stateFlow
            .onEach(_stateFlow::emit)
            .launchIn(coroutineScope)

        if (eventProducerWrapper != null) {
            stateFlow
                .drop(1)
                .zip(effectFlow) { state, effect -> state to effect }
                .flatMapMerge { (state, effect) ->
                    eventProducerWrapper.produce(state, effect)
                        ?.let(::flowOf)
                        ?: emptyFlow()
                }
                .onEach(_eventFlow::emit)
                .launchIn(coroutineScope)
        }
    }

    override fun accept(value: Action) {
        coroutineScope.launch {
            actionFlow.emit(value)
        }
    }

    private class ReducerWrapper<State, Effect>(
        private val reducer: Reducer<State, Effect>,
        private val errorHandler: ErrorHandler<State>
    ) : Reducer<State, Effect> {

        override fun reduce(state: State, effect: Effect): State = try {
            reducer.reduce(state, effect)
        } catch (exception: Exception) {
            errorHandler.handle(state, exception)
            state
        }
    }

    private class MiddlewareWrapper<Action, State, Effect>(
        private val middleware: Middleware<Action, State, Effect>,
        private val errorHandler: ErrorHandler<State>
    ) : Middleware<Action, State, Effect> {

        override fun execute(action: Action, state: State): Flow<Effect> = middleware.execute(action, state)
            .catch { throwable ->
                errorHandler.handle(state, throwable)
                emitAll(emptyFlow())
            }
    }

    private class BootstrapperWrapper<Action, State>(
        private val bootstrapper: Bootstrapper<Action>,
        private val errorHandler: ErrorHandler<State>,
        private val state: State
    ) : Bootstrapper<Action> {

        override fun bootstrap(): Flow<Action> = bootstrapper.bootstrap()
            .catch { throwable ->
                errorHandler.handle(state, throwable)
                emitAll(emptyFlow())
            }
    }

    private class EventProducerWrapper<State, Effect, Event>(
        private val eventProducer: EventProducer<State, Effect, Event>,
        private val errorHandler: ErrorHandler<State>
    ) : EventProducer<State, Effect, Event> {

        override fun produce(state: State, effect: Effect): Event? = try {
            eventProducer.produce(state, effect)
        } catch (exception: Exception) {
            errorHandler.handle(state, exception)
            null
        }
    }
}
