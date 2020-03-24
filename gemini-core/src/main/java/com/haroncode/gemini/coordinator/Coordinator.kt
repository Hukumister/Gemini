package com.haroncode.gemini.coordinator

import com.haroncode.gemini.connector.StoreConnector
import com.haroncode.gemini.connector.StoreLifecycle
import com.haroncode.gemini.connector.StoreLifecycle.Event
import io.reactivex.subscribers.DisposableSubscriber

/**
 * @author HaronCode.
 */
class Coordinator(
    private val lifecycle: StoreLifecycle,
    storeConnector: StoreConnector
) {

    private val stateSubscriber = LifecycleStateSubscriber(storeConnector)

    fun start() = lifecycle.subscribe(stateSubscriber)

    class LifecycleStateSubscriber(
        private val storeConnector: StoreConnector
    ) : DisposableSubscriber<Event>() {

        override fun onComplete() = onNext(Event.COMPLETE)

        override fun onNext(event: Event) = when (event) {
            Event.START -> storeConnector.connect()
            Event.STOP -> storeConnector.disconnect()
            Event.COMPLETE -> storeConnector.dispose()
        }

        override fun onError(throwable: Throwable) = onNext(Event.COMPLETE)
    }
}
