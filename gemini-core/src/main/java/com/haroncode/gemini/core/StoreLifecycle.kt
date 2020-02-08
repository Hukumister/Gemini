package com.haroncode.gemini.core

import org.reactivestreams.Publisher

interface StoreLifecycle : Publisher<StoreLifecycle.State> {

    enum class State {
        START,
        STOP
    }
}
