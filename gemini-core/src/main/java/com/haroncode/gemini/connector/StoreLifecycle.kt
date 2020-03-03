package com.haroncode.gemini.connector

import org.reactivestreams.Publisher

/**
 * @author HaronCode.
 */
interface StoreLifecycle : Publisher<StoreLifecycle.Event> {

    /**
     * Used to trigger the start and stop of connections.
     */
    enum class Event {
        START,
        STOP,
        COMPLETE
    }
}
