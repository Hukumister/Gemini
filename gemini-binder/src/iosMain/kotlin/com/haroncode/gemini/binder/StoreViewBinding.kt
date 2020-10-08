package com.haroncode.gemini.binder

import com.haroncode.gemini.StoreViewDelegate
import com.haroncode.gemini.binder.rule.BindingRulesFactory

actual object StoreViewBinding {

    fun <Action : Any, State : Any> with(
        factoryProvider: () -> BindingRulesFactory<StoreViewDelegate<Action, State>>
    ): Binder<StoreViewDelegate<Action, State>> = BinderImpl(factoryProvider)
}
