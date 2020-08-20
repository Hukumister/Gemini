package com.haroncode.gemini.element

import kotlinx.coroutines.flow.Flow

/**
 * @author HaronCode
 * @author kdk96
 */
typealias Bootstrapper<Action> = () -> Flow<Action>
