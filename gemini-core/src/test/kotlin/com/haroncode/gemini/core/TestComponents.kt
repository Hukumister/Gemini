package com.haroncode.gemini.core

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Subscriber
import com.haroncode.gemini.core.TestAction.*
import com.haroncode.gemini.core.TestEffect.*
import com.haroncode.gemini.core.elements.Bootstrapper
import com.haroncode.gemini.core.elements.Middleware
import com.haroncode.gemini.core.elements.Reducer
import java.util.concurrent.TimeUnit

/**
 * @author HaronCode.
 */

const val INITIAL_COUNTER = 10
const val INITIAL_LOADING = false

const val DELAYED_FULFILL_AMOUNT = 5

const val CONDITIONAL_MULTIPLIER = 10

data class TestState(
        val id: Long = 1L,
        val counter: Int = INITIAL_COUNTER,
        val loading: Boolean = INITIAL_LOADING
)

sealed class TestAction {
    object Unfulfillable : TestAction()
    object FulfillableInstantly : TestAction()
    data class FulfillableAsync(val delayMs: Long) : TestAction()
    object TranslatesTo3Effects : TestAction()
    object MaybeFulfillable : TestAction()
}

sealed class TestViewEvent

sealed class TestEffect {
    data class InstantEffect(val amount: Int) : TestEffect()
    object StartedAsync : TestEffect()
    data class FinishedAsync(val amount: Int) : TestEffect()
    object MultipleEffect1 : TestEffect()
    object MultipleEffect2 : TestEffect()
    object MultipleEffect3 : TestEffect()
    data class ConditionalThingHappened(val multiplier: Int) : TestEffect()
}

class TestReducer : Reducer<TestState, TestEffect> {

    override fun invoke(state: TestState, effect: TestEffect): TestState = when (effect) {
        is InstantEffect -> state.copy(counter = state.counter + effect.amount)
        is FinishedAsync -> state.copy(counter = state.counter + effect.amount, loading = false)
        is ConditionalThingHappened -> state.copy(counter = state.counter * effect.multiplier)
        StartedAsync -> state.copy(loading = true)
        MultipleEffect1,
        MultipleEffect2,
        MultipleEffect3 -> state.copy(counter = state.counter + 1)
    }
}

class TestMiddleware(
    private val asyncWorkScheduler: Scheduler
) : Middleware<TestAction, TestState, TestEffect> {

    override fun invoke(action: TestAction, state: TestState): Flowable<TestEffect> = when (action) {
        Unfulfillable -> Flowable.empty()
        FulfillableInstantly -> Flowable.just(InstantEffect(1))
        is FulfillableAsync -> Flowable.just(DELAYED_FULFILL_AMOUNT)
            .delay(action.delayMs, TimeUnit.MILLISECONDS, asyncWorkScheduler)
            .map<TestEffect>(::FinishedAsync)
            .startWith(StartedAsync)
        TranslatesTo3Effects -> Flowable.just(
            MultipleEffect1,
            MultipleEffect2,
            MultipleEffect3
        )
        MaybeFulfillable ->
            if (state.counter % 3 == 0) Flowable.just<TestEffect>(ConditionalThingHappened(CONDITIONAL_MULTIPLIER))
            else Flowable.empty<TestEffect>()
    }
}

class TestBootstrapper : Bootstrapper<TestAction> {

    override fun invoke(): Observable<TestAction> {
        return Observable.empty()
    }
}


class TestView(
        private val testActionSubject: PublishProcessor<TestAction>,
        private val testStateObserver: TestObserver<TestState>
) : StoreView<TestAction, TestState> {

    override fun accept(state: TestState) {
        testStateObserver.onNext(state)
    }

    override fun subscribe(subscriber: Subscriber<in TestAction>) {
        testActionSubject
            .subscribe(subscriber)
    }
}