package com.haroncode.gemini.sample.presentation.justreducer

import com.haroncode.gemini.android.binder.DelegateBindingRulesFactory
import com.haroncode.gemini.android.binder.bindingRulesFactory
import com.haroncode.gemini.binder.BindingRulesFactory
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.ui.CounterFragment
import javax.inject.Inject

@PerFragment
class CounterBindingFactory @Inject constructor(
    private val store: CounterStore
) : DelegateBindingRulesFactory<CounterFragment>() {

    override val bindingRulesFactory: BindingRulesFactory<CounterFragment> = bindingRulesFactory { view ->
        baseRule { store to view }
        autoCancel { store } // magic is here )))
    }
}
