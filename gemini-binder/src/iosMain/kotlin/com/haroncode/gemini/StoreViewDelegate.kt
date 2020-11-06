package com.haroncode.gemini

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

actual open class StoreViewDelegate<Action : Any, State : Any>(
    private val stateConsumer: (State) -> Unit
) : StoreView<Action, State> {

    internal val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    internal var lifecycleObserver: LifecycleObserver? = null

    private val actionChannel = Channel<Action>(Channel.BUFFERED)

    override val actionFlow: Flow<Action> = actionChannel.receiveAsFlow()

    override fun accept(value: State) {
        stateConsumer.invoke(value)
    }

    actual fun emit(action: Action) {
        coroutineScope.launch {
            actionChannel.send(action)
        }
    }

    fun start() {
        lifecycleObserver?.start()
    }

    fun stop() {
        lifecycleObserver?.stop()
    }

    fun destroy() {
        lifecycleObserver?.destroy()
    }

    internal interface LifecycleObserver {
        fun start()
        fun stop()
        fun destroy()
    }
}
