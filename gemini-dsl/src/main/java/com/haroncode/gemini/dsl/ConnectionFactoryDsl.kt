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

    private val mutableList = mutableListOf<ConnectionRule>()

    fun connection(connectionRule: () -> ConnectionRule) {
        connectionRule.invoke()
            .let(mutableList::add)
    }

    fun autoDispose(storeFactory: () -> Store<*, *, *>) {
        storeFactory.invoke()
            .let(::AutoStoreDisposeConnectionRule)
            .let(mutableList::add)
    }

    fun build() = mutableList.toList()
}