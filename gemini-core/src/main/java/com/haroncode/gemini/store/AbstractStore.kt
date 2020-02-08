package com.haroncode.gemini.store

import com.haroncode.gemini.core.Store
import com.haroncode.gemini.core.elements.Bootstrapper
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
    private val reducer: Reducer<State, Effect>,
    private val middleware: Middleware<Action, State, Effect>,
    bootstrapper: Bootstrapper<Action>? = null,
    private val eventProducer: EventProducer<State, Effect, Event>? = null
) : Store<Action, State, Event> {

    private val compositeDisposable = CompositeDisposable()

    private val stateProcessor = BehaviorProcessor.createDefault(initialState)
    private val actionProcessor = PublishProcessor.create<Action>()
    private val effectProcessor = PublishProcessor.create<Effect>()
    private val eventProcessor = BehaviorProcessor.create<Event>()

    init {
        Flowables.zip(
            stateProcessor,
            effectProcessor
        )
            .map { (state, effect) -> reducer.invoke(state, effect) }
            .subscribe(stateProcessor::onNext, ::onError)
            .addTo(compositeDisposable)

        actionProcessor
            .withLatestFrom(stateProcessor)
            .flatMap { (action, state) -> middleware.invoke(action, state) }
            .subscribe(effectProcessor::onNext, ::onError)
            .addTo(compositeDisposable)

        Flowables.zip(
            stateProcessor.skip(1), //skip initial state
            effectProcessor
        )
            .flatMap { (state, effect) -> produceEventFlowable(state, effect) }
            .subscribe(eventProcessor::onNext, ::onError)
            .addTo(compositeDisposable)

        bootstrapper?.invoke()
            ?.subscribe(::accept, ::onError)
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
    ): Flowable<Event> = eventProducer?.invoke(state, effect)
        ?.let { event -> Flowable.just(event) }
        ?: Flowable.empty<Event>()

    protected open fun onError(throwable: Throwable) = Unit

}