package com.haroncode.gemini.sample.presentation.routing

import com.haroncode.gemini.android.binder.DelegateBindingRulesFactory
import com.haroncode.gemini.android.binder.bindingRulesFactory
import com.haroncode.gemini.binder.BindingRulesFactory
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.ui.MainFragment
import javax.inject.Inject

@PerFragment
class MainBindingFactory @Inject constructor(
    private val store: MainStore,
) : DelegateBindingRulesFactory<MainFragment>() {

    override val bindingRulesFactory: BindingRulesFactory<MainFragment> = bindingRulesFactory { view ->
        baseRule { store to view }
        autoCancel { store } // magic is here )))
    }
}
