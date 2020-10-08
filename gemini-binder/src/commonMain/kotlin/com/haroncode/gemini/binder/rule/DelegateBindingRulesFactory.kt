package com.haroncode.gemini.binder.rule

import com.haroncode.gemini.StoreView
import com.haroncode.gemini.element.Store

/**
 * @author HaronCode
 * @author kdk96
 */
abstract class DelegateBindingRulesFactory<T : Any> : BindingRulesFactory<T> {

    abstract val bindingRulesFactory: BindingRulesFactory<T>

    override fun create(param: T): Collection<BindingRule> = bindingRulesFactory.create(param)
}

inline fun <reified T : Any> bindingRulesFactory(
    crossinline factory: BindingRuleListBuilder.(param: T) -> Unit
): BindingRulesFactory<T> = BindingRulesFactory { param ->
    val builder = BindingRuleListBuilder()
    builder.factory(param)
    builder.build()
}

class BindingRuleListBuilder {

    private val rules = mutableListOf<BindingRule>()

    fun <Action : Any, State : Any> baseRule(
        storeViewProvider: () -> Pair<Store<Action, State, *>, StoreView<Action, State>>
    ) {
        val (store, storeView) = storeViewProvider.invoke()
        rules += store bindStateTo storeView
        rules += storeView bindActionTo store
    }

    fun rule(bindingRuleProvider: () -> BindingRule) {
        rules += bindingRuleProvider.invoke()
    }

    fun autoCancel(storeProvider: () -> Store<*, *, *>) {
        rules += AutoCancelStoreRule(storeProvider.invoke())
    }

    fun build() = rules.toList()
}
