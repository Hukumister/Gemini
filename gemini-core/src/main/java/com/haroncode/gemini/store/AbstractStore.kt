package com.haroncode.gemini.store

import com.haroncode.gemini.core.Store
import com.haroncode.gemini.core.elements.Bootstrapper
import com.haroncode.gemini.core.elements.ErrorHandler
import com.haroncode.gemini.core.elements.EventProducer
import com.haroncode.gemini.core.elements.Middleware
import com.haroncode.gemini.core.elements.Reducer
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.withLatestFrom
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber

/**
 * @author HaronCode.
 */
abstract class AbstractStore<Action : Any, State : Any, Event : Any, Effect : Any>(
    initialState: State,
    reducer: Reducer<State, Effect>,
    middleware: Middleware<Action, State, Effect>,
    bootstrapper: Bootstrapper<Action>? = null,
    eventProducer: EventProducer<State, Effect, Event>? = null,
    errorHandler: ErrorHandler<State>? = null
) : Store<Action, State, Event> {

    private val compositeDisposable = CompositeDisposable()

    private val stateProcessor = BehaviorProcessor.createDefault(initialState)
    private val actionProcessor = PublishProcessor.create<Action>()
    private val effectProcessor = PublishProcessor.create<Effect>()
    private val eventProcessor = PublishProcessor.create<Event>()

    private val reducerWrapper = errorHandler?.let { ReducerWrapper(reducer, errorHandler) } ?: reducer
    private val middlewareWrapper = errorHandler?.let { MiddlewareWrapper(middleware, errorHandler) } ?: middleware
    private val bootstrapperWrapper = errorHandler?.let {
        bootstrapper?.let { BootstrapperWrapper(bootstrapper, errorHandler, initialState) }
    } ?: bootstrapper
    private val eventProducerWrapper = errorHandler?.let {
        eventProducer?.let { EventProducerWrapper(eventProducer, errorHandler) }
    } ?: eventProducer

    init {
        Flowables.zip(
            stateProcessor,
            effectProcessor
        ) { state, effect -> reducerWrapper.invoke(state, effect) }
            .subscribe(stateProcessor::onNext)
            .addTo(compositeDisposable)

        actionProcessor
            .withLatestFrom(stateProcessor)
            .flatMap { (action, state) -> middlewareWrapper.invoke(action, state) }
            .subscribe(effectProcessor::onNext)
            .addTo(compositeDisposable)

        Flowables.zip(
            stateProcessor.skip(1), // skip initial state
            effectProcessor
        )
            .flatMap { (state, effect) -> produceEventFlowable(state, effect) }
            .subscribe(eventProcessor::onNext)
            .addTo(compositeDisposable)

        bootstrapperWrapper?.invoke()
            ?.let { publisher -> Flowable.fromPublisher(publisher) }
            ?.subscribe(::accept)
            ?.addTo(compositeDisposable)
    }

    override val eventSource: Publisher<Event>
        get() = eventProcessor.hide()

    override fun subscribe(subscriber: Subscriber<in State>) = stateProcessor
        .distinctUntilChanged()
        .subscribe(subscriber)

    override fun accept(action: Action) = actionProcessor.onNext(action)

    override fun isDisposed() = compositeDisposable.isDisposed

    override fun dispose() = compositeDisposable.dispose()

    private fun produceEventFlowable(
        state: State,
        effect: Effect
    ): Flowable<Event> = eventProducerWrapper?.invoke(state, effect)
        ?.let { event -> Flowable.just(event) }
        ?: Flowable.empty<Event>()

    private class ReducerWrapper<State, Effect>(
        private val reducer: Reducer<State, Effect>,
        private val errorHandler: ErrorHandler<State>
    ) : Reducer<State, Effect> {

        override fun invoke(state: State, effect: Effect): State = try {
            reducer.invoke(state, effect)
        } catch (exception: Exception) {
            errorHandler.invoke(state, exception)
            state
        }
    }

    private class MiddlewareWrapper<Action, State, Effect>(
        private val middleware: Middleware<Action, State, Effect>,
        private val errorHandler: ErrorHandler<State>
    ) : Middleware<Action, State, Effect> {

        override fun invoke(action: Action, state: State): Publisher<Effect> =
            Flowable.fromPublisher(middleware.invoke(action, state))
                .doOnError { throwable -> errorHandler.invoke(state, throwable) }
                .onErrorResumeNext(Flowable.empty())
    }

    private class BootstrapperWrapper<Action, State>(
        private val bootstrapper: Bootstrapper<Action>,
        private val errorHandler: ErrorHandler<State>,
        private val state: State
    ) : Bootstrapper<Action> {

        override fun invoke(): Publisher<Action> =
            Flowable.fromPublisher(bootstrapper.invoke())
                .doOnError { throwable -> errorHandler.invoke(state, throwable) }
                .onErrorResumeNext(Flowable.empty())
    }

    private class EventProducerWrapper<State, Effect, Event>(
        private val eventProducer: EventProducer<State, Effect, Event>,
        private val errorHandler: ErrorHandler<State>
    ) : EventProducer<State, Effect, Event> {

        override fun invoke(state: State, effect: Effect): Event? = try {
            eventProducer.invoke(state, effect)
        } catch (exception: Exception) {
            errorHandler.invoke(state, exception)
            null
        }
    }
}
