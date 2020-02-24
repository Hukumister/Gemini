package com.haroncode.gemini.core.elements

/**
 * @author HaronCode.
 */
typealias Reducer<State, Effect> = (state: State, effect: Effect) -> State
