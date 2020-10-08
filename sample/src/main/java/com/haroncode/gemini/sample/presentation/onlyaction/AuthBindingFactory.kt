package com.haroncode.gemini.sample.presentation.onlyaction

import com.haroncode.gemini.binder.rule.BindingRulesFactory
import com.haroncode.gemini.binder.rule.DelegateBindingRulesFactory
import com.haroncode.gemini.binder.rule.bindEventTo
import com.haroncode.gemini.binder.rule.bindingRulesFactory
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.ui.AuthFragment
import javax.inject.Inject

@PerFragment
class AuthBindingFactory @Inject constructor(
    private val store: AuthStore
) : DelegateBindingRulesFactory<AuthFragment>() {

    override val bindingRulesFactory: BindingRulesFactory<AuthFragment> = bindingRulesFactory { view ->
        baseRule { store to view }
        rule { store bindEventTo view }
        autoCancel { store } // magic is here )))
    }
}
