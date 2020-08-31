package com.haroncode.gemini.sample.presentation.onlyaction

import com.haroncode.gemini.android.binder.DelegateConnectionRulesFactory
import com.haroncode.gemini.android.binder.connectionRulesFactory
import com.haroncode.gemini.connector.ConnectionRule
import com.haroncode.gemini.connector.bindEventTo
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.ui.AuthFragment
import javax.inject.Inject

@PerFragment
class AuthConnectionFactory @Inject constructor(
    private val store: AuthStore
) : DelegateConnectionRulesFactory<AuthFragment>() {

    override val connectionRulesFactory: ConnectionRule.Factory<AuthFragment> = connectionRulesFactory { view ->
        baseRule { store to view }
        rule { store bindEventTo view }
        autoCancel { store } // magic is here )))
    }
}
