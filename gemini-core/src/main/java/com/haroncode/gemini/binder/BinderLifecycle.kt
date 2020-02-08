package com.haroncode.gemini.binder

import org.reactivestreams.Publisher

interface Lifecycle : Publisher<Lifecycle.State> {

    enum class State {
        START,
        STOP
    }
}
