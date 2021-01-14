package com.haroncode.gemini.sample.presentation.onlyaction

import com.haroncode.gemini.binder.rule.BindingRulesFactory
import com.haroncode.gemini.binder.rule.DelegateBindingRulesFactory
import com.haroncode.gemini.binder.rule.bindEventTo
import com.haroncode.gemini.binder.rule.bindingRulesFactory
import com.haroncode.gemini.sample.ui.AuthFragment
import com.haroncode.gemini.sample.util.getStore
import javax.inject.Inject
import javax.inject.Provider

class AuthBindingFactory @Inject constructor(
    private val storeProvider: Provider<AuthStore>
) : DelegateBindingRulesFactory<AuthFragment>() {

    override val bindingRulesFactory: BindingRulesFactory<AuthFragment> = bindingRulesFactory { view ->
        val store = view.getStore(storeProvider)
        baseRule { store to view }
        rule { store bindEventTo view }
    }
}
