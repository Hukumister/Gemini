package com.haroncode.gemini.sample.presentation.justreducer

import com.haroncode.gemini.binder.BindingRulesFactory
import com.haroncode.gemini.binder.DelegateBindingRulesFactory
import com.haroncode.gemini.binder.bindingRulesFactory
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.ui.CounterStoreViewDelegate
import javax.inject.Inject

@PerFragment
class CounterBindingFactory @Inject constructor(
    private val store: CounterStore
) : DelegateBindingRulesFactory<CounterStoreViewDelegate>() {

    override val bindingRulesFactory: BindingRulesFactory<CounterStoreViewDelegate> = bindingRulesFactory { view ->
        baseRule { store to view }
        autoCancel { store } // magic is here )))
    }
}
