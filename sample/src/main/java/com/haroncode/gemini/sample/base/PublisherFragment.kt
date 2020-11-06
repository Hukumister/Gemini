package com.haroncode.gemini.sample.base

import androidx.annotation.LayoutRes
import androidx.lifecycle.lifecycleScope
import com.haroncode.gemini.StoreView
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class PublisherFragment<Action : Any, ViewState : Any> @JvmOverloads constructor(
    @LayoutRes layoutRes: Int = 0
) : BaseFragment(layoutRes), StoreView<Action, ViewState> {

    private val actionChannel = Channel<Action>(Channel.BUFFERED)

    override val actionFlow: Flow<Action> = actionChannel.receiveAsFlow()

    fun postAction(action: Action) {
        lifecycleScope.launch {
            actionChannel.send(action)
        }
    }

    abstract fun onViewStateChanged(viewState: ViewState)

    override fun accept(value: ViewState) {
        onViewStateChanged(value)
    }
}
