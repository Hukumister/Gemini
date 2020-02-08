package com.haroncode.gemini.binder

import org.reactivestreams.Publisher

interface BinderLifecycle : Publisher<BinderLifecycle.State> {

    enum class State {
        START,
        STOP
    }
}
