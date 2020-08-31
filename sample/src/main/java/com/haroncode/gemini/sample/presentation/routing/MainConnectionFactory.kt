package com.haroncode.gemini.sample.presentation.routing

import com.haroncode.gemini.android.binder.DelegateConnectionRulesFactory
import com.haroncode.gemini.android.binder.connectionRulesFactory
import com.haroncode.gemini.connector.ConnectionRule
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.ui.MainFragment
import javax.inject.Inject

@PerFragment
class MainConnectionFactory @Inject constructor(
    private val store: MainStore,
) : DelegateConnectionRulesFactory<MainFragment>() {

    override val connectionRulesFactory: ConnectionRule.Factory<MainFragment> = connectionRulesFactory { view ->
        baseRule { store to view }
        autoCancel { store } // magic is here )))
    }
}
