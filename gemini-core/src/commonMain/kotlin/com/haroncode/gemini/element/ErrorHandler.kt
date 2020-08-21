package com.haroncode.gemini.element

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface ErrorHandler<State> {

    fun handle(state: State, error: Throwable)
}
