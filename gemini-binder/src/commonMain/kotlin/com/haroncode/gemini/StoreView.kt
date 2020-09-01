package com.haroncode.gemini

import com.haroncode.gemini.functional.Consumer
import kotlinx.coroutines.flow.Flow

/**
 * @author HaronCode
 * @author kdk96
 */
interface StoreView<Action : Any, State : Any> : Consumer<State> {
    val actionFlow: Flow<Action>
}
