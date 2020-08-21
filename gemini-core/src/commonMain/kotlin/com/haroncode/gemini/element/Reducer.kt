package com.haroncode.gemini.element

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface Reducer<State, Effect> {

    fun reduce(state: State, effect: Effect): State
}
