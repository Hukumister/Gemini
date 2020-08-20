package com.haroncode.gemini.element

/**
 * @author HaronCode
 * @author kdk96
 */
typealias ErrorHandler<State> = (State, Throwable) -> Unit
