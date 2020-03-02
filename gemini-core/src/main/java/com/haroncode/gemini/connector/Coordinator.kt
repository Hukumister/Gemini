package com.haroncode.gemini.connector

import io.reactivex.functions.Consumer

/**
 * @author HaronCode.
 */
class Coordinator(
    private val storeConnector: StoreConnector
) : Consumer<StoreLifecycle.Event> {

    override fun accept(event: StoreLifecycle.Event) = when (event) {
        StoreLifecycle.Event.START -> storeConnector.connect()
        StoreLifecycle.Event.STOP -> storeConnector.disconnect()
        StoreLifecycle.Event.COMPLETE -> storeConnector.dispose()
    }
}