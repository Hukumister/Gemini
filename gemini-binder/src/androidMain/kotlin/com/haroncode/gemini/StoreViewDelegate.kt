package com.haroncode.gemini

import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.functional.Consumer
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

actual open class StoreViewDelegate<Action : Any, State : Any>(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    stateConsumer: Consumer<State>
) : StoreView<Action, State>,
    Consumer<State> by stateConsumer,
    SavedStateRegistryOwner by savedStateRegistryOwner {

    private val actionChannel = BroadcastChannel<Action>(Channel.BUFFERED)

    override val actionFlow: Flow<Action> = actionChannel.asFlow()

    actual fun emit(action: Action) {
        actionChannel.offer(action)
    }
}
