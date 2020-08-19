package com.haroncode.gemini.element

import com.haroncode.gemini.util.Consumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * @author HaronCode
 * @author kdk96
 */
interface Store<Action : Any, State : Any, Event : Any> : Consumer<Action> {
    val coroutineScope: CoroutineScope
    val stateFlow: Flow<State>
    val eventFlow: Flow<Event>
}
