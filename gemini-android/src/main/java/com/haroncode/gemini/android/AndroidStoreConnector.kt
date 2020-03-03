package com.haroncode.gemini.android

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.android.lifecycle.StoreLifecycleEventObserver
import com.haroncode.gemini.android.strategies.StartStopStrategy
import com.haroncode.gemini.connection.ConnectionRule
import com.haroncode.gemini.connector.BaseStoreConnector
import com.haroncode.gemini.connector.Coordinator

object AndroidStoreConnector {

    fun <T : LifecycleOwner> withFactory(factory: ConnectionRule.Factory<T>) = ConnectionProcessor(factory)

    class ConnectionProcessor<T : LifecycleOwner>(private val factory: ConnectionRule.Factory<T>) {

        private var lifecycleStrategy: LifecycleStrategy = StartStopStrategy

        fun withStrategy(lifecycleStrategy: LifecycleStrategy): ConnectionProcessor<T> {
            this.lifecycleStrategy = lifecycleStrategy
            return this
        }

        fun connect(view: T) {
            val connectionRules = factory.create(view)
            val storeConnector = BaseStoreConnector(connectionRules)
            val coordinator = Coordinator(storeConnector)

            val storeLifecycle = StoreLifecycleEventObserver(coordinator, lifecycleStrategy)
            view.lifecycle.addObserver(storeLifecycle)
        }
    }
}
