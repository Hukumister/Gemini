package com.haroncode.gemini.element

import com.haroncode.gemini.functional.Consumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author HaronCode
 * @author kdk96
 */
interface Store<Action : Any, State : Any, Event : Any> : Consumer<Action> {
    val coroutineScope: CoroutineScope
    val stateFlow: StateFlow<State>
    val eventFlow: Flow<Event>
}
