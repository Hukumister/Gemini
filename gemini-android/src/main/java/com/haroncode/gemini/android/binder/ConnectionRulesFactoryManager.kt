package com.haroncode.gemini.android.binder

import com.haroncode.gemini.connector.ConnectionRulesFactory

internal object ConnectionRulesFactoryManager {

    private val factories: MutableMap<String, ConnectionRulesFactory<*>> = mutableMapOf()

    fun <T : Any> saveFactory(tag: String, storeFactory: ConnectionRulesFactory<T>) {
        factories[tag] = storeFactory
    }

    fun <T : Any> restoreStore(tag: String): ConnectionRulesFactory<T>? {
        return factories[tag] as? ConnectionRulesFactory<T>
    }

    fun clear(tag: String) {
        factories.remove(tag)
    }
}
