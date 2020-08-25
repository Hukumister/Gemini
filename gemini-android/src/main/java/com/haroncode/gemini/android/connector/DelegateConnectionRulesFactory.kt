package com.haroncode.gemini.android.connector

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.connector.AutoCancelStoreRule
import com.haroncode.gemini.connector.BaseConnectionRule
import com.haroncode.gemini.connector.ConnectionRule
import com.haroncode.gemini.connector.ConnectionRulesFactory
import com.haroncode.gemini.element.Store

/**
 * @author HaronCode
 * @author kdk96
 */
abstract class DelegateConnectionRulesFactory<T : LifecycleOwner> : ConnectionRulesFactory<T> {

    abstract val connectionRulesFactory: ConnectionRulesFactory<T>

    override fun create(param: T): Collection<ConnectionRule> = connectionRulesFactory.create(param)
}

inline fun <reified T : Any> connectionRulesFactory(
    crossinline factory: ConnectionRuleListBuilder.(param: T) -> Unit
): ConnectionRulesFactory<T> = ConnectionRulesFactory { param ->
    val builder = ConnectionRuleListBuilder()
    builder.factory(param)
    builder.build()
}

class ConnectionRuleListBuilder {

    private val connectionRules = mutableListOf<ConnectionRule>()

    fun rule(connectionRule: () -> BaseConnectionRule<*, *>) {
        connectionRule.invoke()
            .let(connectionRules::add)
    }

    fun autoCancel(storeProvider: () -> Store<*, *, *>) {
        storeProvider.invoke()
            .let(::AutoCancelStoreRule)
            .let(connectionRules::add)
    }

    fun build() = connectionRules.toList()
}
