package com.haroncode.gemini.android.binder

import com.haroncode.gemini.connector.ConnectionRule

internal object ConnectionRulesFactoryManager {

    private val factories: MutableMap<String, ConnectionRule.Factory<*>> = mutableMapOf()

    fun <T : Any> saveFactory(tag: String, storeFactory: ConnectionRule.Factory<T>) {
        factories[tag] = storeFactory
    }

    fun <T : Any> restoreStore(tag: String): ConnectionRule.Factory<T>? {
        return factories[tag] as? ConnectionRule.Factory<T>
    }

    fun clear(tag: String) {
        factories.remove(tag)
    }
}
