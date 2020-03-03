package com.haroncode.gemini.connector

import io.reactivex.functions.Consumer
import io.reactivex.subscribers.DisposableSubscriber

/**
 * @author HaronCode.
 */
class LifecycleEventSource(
    private val lifecycle: StoreLifecycle
) {

    fun start(consumer: Consumer<StoreLifecycle.Event>) {
        val stateSubscriber = LifecycleStateSubscriber(consumer)
        lifecycle.subscribe(stateSubscriber)
    }

    class LifecycleStateSubscriber(
        private val consumer: Consumer<StoreLifecycle.Event>
    ) : DisposableSubscriber<StoreLifecycle.Event>() {

        override fun onComplete() = consumer.accept(StoreLifecycle.Event.COMPLETE)

        override fun onNext(event: StoreLifecycle.Event) = consumer.accept(event)

        override fun onError(throwable: Throwable) = consumer.accept(StoreLifecycle.Event.COMPLETE)
    }
}
