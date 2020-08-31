package com.haroncode.gemini.android.binder

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.android.lifecycle.ExtendedLifecycleObserver
import com.haroncode.gemini.android.lifecycle.LifecycleStrategy
import com.haroncode.gemini.android.lifecycle.StoreCancelObserver
import com.haroncode.gemini.connector.AutoCancelStoreRule
import com.haroncode.gemini.connector.BaseConnectionRule
import com.haroncode.gemini.connector.ConnectionRulesFactory

internal class RestoreBinder<View : SavedStateRegistryOwner>(
    private val factoryProvider: () -> ConnectionRulesFactory<View>,
    private val lifecycleStrategy: LifecycleStrategy,
) : Binder<View> {

    override fun bind(view: View) {
        val factoryNameSaver = FactoryNameSaver(view.savedStateRegistry)

        val factoryName = factoryNameSaver.restoreOrCreateName()
        val factory = ConnectionRulesFactoryManager.restoreStore(factoryName) ?: factoryProvider()
        val connectionRules = factory.create(view)

        lifecycleStrategy.connect(view, connectionRules.filterIsInstance<BaseConnectionRule<*, *>>())

        val autoCancelStoreRuleCollection = connectionRules.filterIsInstance<AutoCancelStoreRule>()

        val storeCancelObserver = StoreCancelObserver(autoCancelStoreRuleCollection)
        val clearFactoryDecorator = ClearFactoryDecorator(factoryName, storeCancelObserver)

        view.lifecycle.addObserver(clearFactoryDecorator)

        ConnectionRulesFactoryManager.saveFactory(factoryName, factory)
        factoryNameSaver.saveName(factoryName)
    }

    private class FactoryNameSaver(private val stateRegistry: SavedStateRegistry) {

        companion object {

            private const val TAG = "store_view"

            private const val KEY_FACTORY_SAVER_NAME = "factory_saver_name"
            private const val KEY_FACTORY_NAME = "factory_name"
        }

        fun restoreOrCreateName(): String {
            return if (stateRegistry.isRestored) {
                val bundle = stateRegistry.consumeRestoredStateForKey(KEY_FACTORY_SAVER_NAME)
                bundle?.getString(KEY_FACTORY_NAME) ?: run { createNewName() }
            } else {
                createNewName()
            }
        }

        fun saveName(factoryName: String) {
            stateRegistry.registerSavedStateProvider(KEY_FACTORY_SAVER_NAME) {
                Bundle().apply { putString(KEY_FACTORY_NAME, factoryName) }
            }
        }

        private fun createNewName(): String = "${TAG}_${stateRegistry.hashCode()}"
    }

    private class ClearFactoryDecorator(
        private val name: String,
        private val storeCancelObserver: StoreCancelObserver
    ) : ExtendedLifecycleObserver() {

        override fun onFinish(owner: LifecycleOwner) {
            super.onFinish(owner)
            storeCancelObserver.onFinish(owner)
            ConnectionRulesFactoryManager.clear(name)
        }
    }

}
