package com.haroncode.gemini.binder

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
