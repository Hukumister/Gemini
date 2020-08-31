package com.haroncode.gemini.android.binder

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.StoreView
import com.haroncode.gemini.connector.AutoCancelStoreRule
import com.haroncode.gemini.connector.BaseConnectionRule
import com.haroncode.gemini.connector.ComposeConnectionRule
import com.haroncode.gemini.connector.ConnectionRule
import com.haroncode.gemini.connector.bindActionTo
import com.haroncode.gemini.connector.bindStateTo
import com.haroncode.gemini.element.Store

/**
 * @author HaronCode
 * @author kdk96
 */
abstract class DelegateConnectionRulesFactory<T : LifecycleOwner> : ConnectionRule.Factory<T> {

    abstract val connectionRulesFactory: ConnectionRule.Factory<T>

    override fun create(param: T): ConnectionRule = connectionRulesFactory.create(param)
}

inline fun <reified T : Any> connectionRulesFactory(
    crossinline factory: ConnectionRuleListBuilder.(param: T) -> Unit
): ConnectionRule.Factory<T> = object : ConnectionRule.Factory<T> {

    override fun create(param: T): ConnectionRule {
        val builder = ConnectionRuleListBuilder()
        builder.factory(param)
        return builder.build()
    }
}

class ConnectionRuleListBuilder {

    private val connectionRules = mutableListOf<ConnectionRule>()

    fun <Action : Any, State : Any> baseRule(
        storeViewProvider: () -> Pair<Store<Action, State, *>, StoreView<Action, State>>
    ) {
        val (store, storeView) = storeViewProvider.invoke()
        connectionRules += store bindStateTo storeView
        connectionRules += storeView bindActionTo store
    }

    fun rule(connectionRuleProvider: () -> BaseConnectionRule<*, *>) {
        connectionRules += connectionRuleProvider.invoke()
    }

    fun autoCancel(storeProvider: () -> Store<*, *, *>) {
        connectionRules += AutoCancelStoreRule(storeProvider.invoke())
    }

    fun build() = ComposeConnectionRule(connectionRules.toList())
}
