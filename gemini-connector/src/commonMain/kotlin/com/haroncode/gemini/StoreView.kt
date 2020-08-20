package com.haroncode.gemini

import com.haroncode.gemini.util.Consumer
import kotlinx.coroutines.flow.Flow

/**
 * @author HaronCode
 * @author kdk96
 */
interface StoreView<State : Any, Action : Any> : Consumer<State> {
    val actionFlow: Flow<Action>
}
