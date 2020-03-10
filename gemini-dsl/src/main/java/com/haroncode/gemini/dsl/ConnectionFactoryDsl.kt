package com.haroncode.gemini.dsl

import com.haroncode.gemini.connection.AutoStoreDisposeConnectionRule
import com.haroncode.gemini.connection.ConnectionRule
import com.haroncode.gemini.core.Store

/**
 * @author HaronCode.
 */
inline fun <reified T : Any> connectionFactory(
    crossinline factory: ConnectionRuleListBuilder.(param: T) -> Unit
): ConnectionRule.Factory<T> = object : ConnectionRule.Factory<T> {

    override fun create(param: T): Collection<ConnectionRule> {
        val builder = ConnectionRuleListBuilder()
        builder.factory(param)
        return builder.build()
    }
}

class ConnectionRuleListBuilder {

    private val connectionRules = mutableListOf<ConnectionRule>()

    fun rule(connectionRule: () -> ConnectionRule) {
        connectionRule.invoke()
            .let(connectionRules::add)
    }

    fun autoDispose(storeProvider: () -> Store<*, *, *>) {
        storeProvider.invoke()
            .let(::AutoStoreDisposeConnectionRule)
            .let(connectionRules::add)
    }

    fun build() = connectionRules.toList()
}