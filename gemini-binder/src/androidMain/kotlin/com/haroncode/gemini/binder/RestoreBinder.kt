package com.haroncode.gemini.binder

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.binder.coordinator.Coordinator
import com.haroncode.gemini.binder.rule.AutoCancelStoreRule
import com.haroncode.gemini.binder.rule.BindingRulesFactory
import com.haroncode.gemini.lifecycle.ExtendedLifecycleObserver
import com.haroncode.gemini.lifecycle.StoreCancelObserver
import com.haroncode.gemini.lifecycle.StoreLifecycleObserver
import com.haroncode.gemini.lifecycle.strategy.LifecycleStrategy
import kotlinx.coroutines.Dispatchers
import java.util.UUID

@Deprecated(
    message = "Will be removed in upcoming release, have to use getStore extension to retain and cancel store instance in ViewModel",
    replaceWith = ReplaceWith("getStore(provider)", imports = ["com.haroncode.gemini.keeper.getStore"])
)
internal class RestoreBinder<View : SavedStateRegistryOwner>(
    private val factoryProvider: () -> BindingRulesFactory<View>,
    private val lifecycleStrategy: LifecycleStrategy,
) : Binder<View> {

    override fun bind(view: View) {
        val factoryNameSaver = FactoryNameSaver(view.savedStateRegistry)

        val factoryName = factoryNameSaver.restoreOrCreateName()
        val factory = BindingRulesFactoryManager.restoreFactory(factoryName) ?: factoryProvider()
        val bindingRules = factory.create(view)

        val autoCancelStoreRuleCollection = bindingRules.filterIsInstance<AutoCancelStoreRule>()

        val storeLifecycleObserver = StoreLifecycleObserver(lifecycleStrategy, Coordinator(Dispatchers.Main.immediate, bindingRules))
        val storeCancelObserver = StoreCancelObserver(autoCancelStoreRuleCollection)
        val clearFactoryDecorator = ClearFactoryDecorator(factoryName)

        view.lifecycle.addObserver(storeLifecycleObserver)
        view.lifecycle.addObserver(storeCancelObserver)
        view.lifecycle.addObserver(clearFactoryDecorator)

        BindingRulesFactoryManager.saveFactory(factoryName, factory)
        factoryNameSaver.saveName(factoryName)
    }

    private class FactoryNameSaver(private val stateRegistry: SavedStateRegistry) {

        companion object {

            private const val TAG = "store_view_factory"
            private const val KEY_FACTORY_SAVER_NAME = "gemini.RestoreBinder.FactoryNameSaver.KEY_FACTORY_SAVER_NAME"
            private const val KEY_FACTORY_NAME = "gemini.RestoreBinder.FactoryNameSaver.KEY_FACTORY_NAME"
        }

        fun restoreOrCreateName(): String = if (stateRegistry.isRestored) {
            val bundle = stateRegistry.consumeRestoredStateForKey(KEY_FACTORY_SAVER_NAME)
            bundle?.getString(KEY_FACTORY_NAME) ?: createNewName()
        } else {
            createNewName()
        }

        fun saveName(factoryName: String) = try {
            saveNameInternal(factoryName)
        } catch (ex: IllegalArgumentException) {
            throw IllegalStateException("It's forbidden to use several factories on one fragment/activity")
        }

        private fun saveNameInternal(factoryName: String) {
            stateRegistry.registerSavedStateProvider(KEY_FACTORY_SAVER_NAME) {
                Bundle().apply { putString(KEY_FACTORY_NAME, factoryName) }
            }
        }

        private fun createNewName(): String = "${TAG}_${UUID.randomUUID()}"
    }

    private class ClearFactoryDecorator(
        private val name: String
    ) : ExtendedLifecycleObserver() {

        override fun onFinish(owner: LifecycleOwner) {
            BindingRulesFactoryManager.clear(name)
        }
    }
}
