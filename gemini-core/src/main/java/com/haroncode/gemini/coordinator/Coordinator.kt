package com.haroncode.gemini.coordinator

import com.haroncode.gemini.connector.StoreConnector
import com.haroncode.gemini.connector.StoreLifecycle.Event

/**
 * @author HaronCode.
 */
private typealias EventConsumer = (Event) -> Unit

class Coordinator(
    private val storeConnector: StoreConnector
) : EventConsumer {

    override fun invoke(event: Event) = when (event) {
        Event.START -> storeConnector.connect()
        Event.STOP -> storeConnector.disconnect()
        Event.COMPLETE -> storeConnector.dispose()
    }
}
