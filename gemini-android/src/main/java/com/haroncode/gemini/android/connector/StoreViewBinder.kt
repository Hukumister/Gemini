package com.haroncode.gemini.android.connector

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.android.lifecycle.ExtendedLifecycleObserver
import com.haroncode.gemini.android.lifecycle.LifecycleStrategy
import com.haroncode.gemini.android.lifecycle.StartStopStrategy
import com.haroncode.gemini.android.lifecycle.StoreCancelObserver
import com.haroncode.gemini.connector.AutoCancelStoreRule
import com.haroncode.gemini.connector.BaseConnectionRule
import com.haroncode.gemini.connector.ConnectionRulesFactory

/**
 * @author HaronCode
 * @author kdk96
 */
class StoreViewBinder<T : SavedStateRegistryOwner> private constructor(
    private val factoryProvider: () -> ConnectionRulesFactory<T>,
    private val lifecycleStrategy: LifecycleStrategy,
    private val shouldSave: Boolean
) {

    fun bind(view: T) = if (shouldSave) {
        restoreBind(view)
    } else {
        simpleBind(view)
    }

    private fun restoreBind(view: T) {
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

    private fun simpleBind(view: T) {
        val connectionRules = factoryProvider().create(view)

        lifecycleStrategy.connect(view, connectionRules.filterIsInstance<BaseConnectionRule<*, *>>())

        val autoCancelStoreRuleCollection = connectionRules.filterIsInstance<AutoCancelStoreRule>()
        view.lifecycle.addObserver(StoreCancelObserver(autoCancelStoreRuleCollection))
    }

    companion object {

        fun <T : SavedStateRegistryOwner> of(
            lifecycleStrategy: LifecycleStrategy = StartStopStrategy,
            shouldSave: Boolean = true,
            factoryProvider: () -> ConnectionRulesFactory<T>
        ) = StoreViewBinder(
            factoryProvider = factoryProvider,
            lifecycleStrategy = lifecycleStrategy,
            shouldSave = shouldSave
        )
    }

    internal class FactoryNameSaver(private val stateRegistry: SavedStateRegistry) {

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

    internal class ClearFactoryDecorator(
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
