package com.haroncode.gemini

import com.haroncode.gemini.element.ErrorHandler

class TestErrorHandler<State> : ErrorHandler<State> {

    var lastState: State? = null
    var lastThrowable: Throwable? = null

    override fun handle(state: State, throwable: Throwable) {
        lastState = state
        lastThrowable = throwable
    }
}
