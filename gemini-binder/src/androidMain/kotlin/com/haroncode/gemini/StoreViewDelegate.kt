package com.haroncode.gemini

import androidx.lifecycle.lifecycleScope
import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.functional.Consumer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

actual open class StoreViewDelegate<Action : Any, State : Any>(
    savedStateRegistryOwner: SavedStateRegistryOwner,
    stateConsumer: Consumer<State>
) : StoreView<Action, State>,
    Consumer<State> by stateConsumer,
    SavedStateRegistryOwner by savedStateRegistryOwner {

    private val actionChannel = Channel<Action>(Channel.BUFFERED)

    override val actionFlow: Flow<Action> = actionChannel.receiveAsFlow()

    actual fun emit(action: Action) {
        lifecycleScope.launch {
            actionChannel.send(action)
        }
    }
}
