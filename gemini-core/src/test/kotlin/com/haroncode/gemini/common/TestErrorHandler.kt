package com.haroncode.gemini.common

import com.haroncode.gemini.core.elements.ErrorHandler

class TestErrorHandler<State> : ErrorHandler<State> {

    var lastState: State? = null
    var lastThrowable: Throwable? = null

    override fun invoke(state: State, throwable: Throwable) {
        lastState = state
        lastThrowable = throwable
    }
}
