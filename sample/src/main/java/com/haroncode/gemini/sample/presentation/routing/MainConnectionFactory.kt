package com.haroncode.gemini.sample.presentation.routing

import com.haroncode.gemini.android.connector.DelegateConnectionRulesFactory
import com.haroncode.gemini.android.connector.connectionRulesFactory
import com.haroncode.gemini.connector.ConnectionRulesFactory
import com.haroncode.gemini.sample.ui.MainFragment
import javax.inject.Inject

class MainConnectionFactory @Inject constructor(
    private val store: MainStore,
) : DelegateConnectionRulesFactory<MainFragment>() {

    override val connectionRulesFactory: ConnectionRulesFactory<MainFragment> = connectionRulesFactory { view ->
        baseRule { store to view }
        autoCancel { store } // magic is here )))
    }
}
