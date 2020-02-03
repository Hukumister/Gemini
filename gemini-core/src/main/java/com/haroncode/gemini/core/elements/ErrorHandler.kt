package com.haroncode.gemini.core.elements

/**
 * @author kdk96.
 */
typealias ErrorHandler<State> = (State, Throwable) -> Unit
