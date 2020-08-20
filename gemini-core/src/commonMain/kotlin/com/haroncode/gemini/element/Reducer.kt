package com.haroncode.gemini.element

/**
 * @author HaronCode
 * @author kdk96
 */
typealias Reducer<State, Effect> = (state: State, effect: Effect) -> State
