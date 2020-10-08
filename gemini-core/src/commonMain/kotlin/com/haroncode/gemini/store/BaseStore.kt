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
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
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

    private val stateChannel = ConflatedBroadcastChannel(initialState)
    private val actionChannel = BroadcastChannel<Action>(Channel.BUFFERED)
    private val eventChannel = BroadcastChannel<Event>(Channel.BUFFERED)

    final override val stateFlow: Flow<State> = stateChannel.asFlow()

    final override val eventFlow: Flow<Event> = eventChannel.asFlow()

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

        val effectChannel = BroadcastChannel<Effect>(Channel.BUFFERED)
        val effectFlow = effectChannel.asFlow()

        stateFlow.zip(effectFlow) { state, effect ->
            reducerWrapper.reduce(state, effect)
        }
            .onEach(stateChannel::send)
            .launchIn(coroutineScope)

        merge(
            actionChannel.asFlow(),
            bootstrapperWrapper?.bootstrap() ?: emptyFlow()
        )
            .flatMapMerge { action -> middlewareWrapper.execute(action, stateChannel.value) }
            .onEach(effectChannel::send)
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
                .onEach(eventChannel::send)
                .launchIn(coroutineScope)
        }
    }

    override fun accept(value: Action) {
        coroutineScope.launch {
            actionChannel.send(value)
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
