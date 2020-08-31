package com.haroncode.gemini.android.binder

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.StoreView
import com.haroncode.gemini.connector.AutoCancelStoreRule
import com.haroncode.gemini.connector.BaseConnectionRule
import com.haroncode.gemini.connector.ConnectionRule
import com.haroncode.gemini.connector.ConnectionRulesFactory
import com.haroncode.gemini.connector.bindActionTo
import com.haroncode.gemini.connector.bindStateTo
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

    fun build() = connectionRules.toList()
}
