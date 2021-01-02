package com.haroncode.gemini.sample.presentation.justreducer

import com.haroncode.gemini.binder.rule.BindingRulesFactory
import com.haroncode.gemini.binder.rule.DelegateBindingRulesFactory
import com.haroncode.gemini.binder.rule.bindingRulesFactory
import com.haroncode.gemini.sample.ui.CounterStoreViewDelegate
import com.haroncode.gemini.sample.util.getStore
import javax.inject.Inject
import javax.inject.Provider

class CounterBindingFactory @Inject constructor(
    private val storeProvider: Provider<CounterStore>
) : DelegateBindingRulesFactory<CounterStoreViewDelegate>() {

    override val bindingRulesFactory: BindingRulesFactory<CounterStoreViewDelegate> = bindingRulesFactory { view ->
        val counterStore = view.getStore(storeProvider)
        baseRule { counterStore to view }
    }
}
