package com.haroncode.gemini.android.connector.dsl

import com.haroncode.gemini.connector.AutoCancelStoreRule
import com.haroncode.gemini.connector.ConnectionRule
import com.haroncode.gemini.connector.ConnectionRulesFactory
import com.haroncode.gemini.element.Store

/**
 * @author HaronCode
 * @author kdk96
 */
inline fun <reified T : Any> connectionRulesFactory(
    crossinline factory: ConnectionRuleListBuilder.(param: T) -> Unit
): ConnectionRulesFactory<T> = ConnectionRulesFactory { param ->
    val builder = ConnectionRuleListBuilder()
    builder.factory(param)
    builder.build()
}

class ConnectionRuleListBuilder {

    private val connectionRules = mutableListOf<ConnectionRule>()

    fun rule(connectionRule: () -> ConnectionRule) {
        connectionRule.invoke()
            .let(connectionRules::add)
    }

    fun autoCancel(storeProvider: () -> Store<*, *, *>) {
        storeProvider.invoke()
            .let(::AutoCancelStoreRule)
            .let(connectionRules::add)
    }

    fun build() = connectionRules
}
