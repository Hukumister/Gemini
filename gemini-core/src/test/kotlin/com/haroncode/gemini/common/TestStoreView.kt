package com.haroncode.gemini.common

import com.haroncode.gemini.core.StoreView
import io.reactivex.observers.TestObserver
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Subscriber

class TestStoreView(
    private val testActionProcessor: PublishProcessor<TestAction>,
    private val testStateObserver: TestObserver<TestState>
) : StoreView<TestAction, TestState> {

    override fun accept(state: TestState) = testStateObserver.onNext(state)

    override fun subscribe(subscriber: Subscriber<in TestAction>) = testActionProcessor.subscribe(subscriber)
}
