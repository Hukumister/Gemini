package com.haroncode.gemini.sample.base

import androidx.annotation.LayoutRes
import com.haroncode.gemini.StoreView
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

abstract class PublisherFragment<Action : Any, ViewState : Any> @JvmOverloads constructor(
    @LayoutRes layoutRes: Int = 0
) : BaseFragment(layoutRes), StoreView<Action, ViewState> {

    protected val broadcastChannel = BroadcastChannel<Action>(Channel.BUFFERED)

    fun postAction(action: Action) = broadcastChannel.offer(action)

    abstract fun onViewStateChanged(viewState: ViewState)

    override val actionFlow: Flow<Action> = broadcastChannel.asFlow()

    override fun accept(value: ViewState) {
        onViewStateChanged(value)
    }
}
