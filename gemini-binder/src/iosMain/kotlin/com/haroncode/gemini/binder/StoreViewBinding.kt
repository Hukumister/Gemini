package com.haroncode.gemini.binder

import com.haroncode.gemini.StoreViewDelegate

actual object StoreViewBinding {

    fun <Action : Any, State : Any> with(
        factoryProvider: () -> BindingRulesFactory<StoreViewDelegate<Action, State>>
    ): Binder<StoreViewDelegate<Action, State>> = BinderImpl(factoryProvider)
}
