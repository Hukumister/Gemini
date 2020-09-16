package com.haroncode.gemini.element

import kotlinx.coroutines.flow.Flow

/**
 * @author HaronCode
 * @author kdk96
 */
fun interface Bootstrapper<Action> {

    fun bootstrap(): Flow<Action>
}
