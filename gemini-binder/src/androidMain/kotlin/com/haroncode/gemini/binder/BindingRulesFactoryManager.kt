package com.haroncode.gemini.binder

import com.haroncode.gemini.binder.rule.BindingRulesFactory

@Deprecated(
    message = "Will be removed in upcoming release, have to use getStore extension to retain and cancel store instance in ViewModel",
    replaceWith = ReplaceWith("getStore(provider)", imports = ["com.haroncode.gemini.keeper.getStore"])
)
internal object BindingRulesFactoryManager {

    private val factories: MutableMap<String, BindingRulesFactory<*>> = mutableMapOf()

    fun <T : Any> saveFactory(tag: String, factory: BindingRulesFactory<T>) {
        factories[tag] = factory
    }

    fun <T : Any> restoreFactory(tag: String): BindingRulesFactory<T>? {
        @Suppress("UNCHECKED_CAST")
        return factories[tag] as? BindingRulesFactory<T>
    }

    fun clear(tag: String) {
        factories.remove(tag)
    }
}
