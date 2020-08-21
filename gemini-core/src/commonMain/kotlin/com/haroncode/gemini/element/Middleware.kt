package com.haroncode.gemini.element

import kotlinx.coroutines.flow.Flow

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface Middleware<Action, State, Effect> {

    fun execute(action: Action, state: State): Flow<Effect>
}
