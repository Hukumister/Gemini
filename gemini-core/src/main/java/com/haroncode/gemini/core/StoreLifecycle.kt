package com.haroncode.gemini.core

import org.reactivestreams.Publisher

interface StoreLifecycle : Publisher<StoreLifecycle.Event> {

    enum class Event {
        START,
        STOP
    }
}
