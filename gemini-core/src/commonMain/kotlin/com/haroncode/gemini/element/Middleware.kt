package com.haroncode.gemini.element

import kotlinx.coroutines.flow.Flow

/**
 * @author HaronCode
 * @author kdk96
 */
typealias Middleware<Action, State, Effect> = (action: Action, state: State) -> Flow<Effect>
