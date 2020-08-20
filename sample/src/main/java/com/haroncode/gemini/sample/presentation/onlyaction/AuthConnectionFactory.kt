package com.haroncode.gemini.sample.presentation.onlyaction

import com.haroncode.gemini.android.connector.DelegateConnectionRulesFactory
import com.haroncode.gemini.android.connector.dsl.connectTo
import com.haroncode.gemini.android.connector.dsl.connectionRulesFactory
import com.haroncode.gemini.android.connector.dsl.eventTo
import com.haroncode.gemini.android.connector.dsl.stateTo
import com.haroncode.gemini.android.connector.dsl.transform
import com.haroncode.gemini.connector.ConnectionRulesFactory
import com.haroncode.gemini.connector.identityTransformer
import com.haroncode.gemini.sample.ui.AuthFragment
import javax.inject.Inject

class AuthConnectionFactory @Inject constructor(
    private val store: AuthStore
) : DelegateConnectionRulesFactory<AuthFragment>() {

    override val connectionRulesFactory: ConnectionRulesFactory<AuthFragment> = connectionRulesFactory { view ->
        rule { view.actionFlow connectTo store transform identityTransformer() }
        rule { store stateTo view }

        rule { store eventTo view }
        autoCancel { store } // magic is here )))
    }
}
