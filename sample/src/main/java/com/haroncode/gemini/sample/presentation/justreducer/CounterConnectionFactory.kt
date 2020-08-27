package com.haroncode.gemini.sample.presentation.justreducer

import com.haroncode.gemini.android.connector.DelegateConnectionRulesFactory
import com.haroncode.gemini.android.connector.connectionRulesFactory
import com.haroncode.gemini.connector.ConnectionRulesFactory
import com.haroncode.gemini.sample.di.scope.PerFragment
import com.haroncode.gemini.sample.ui.CounterFragment
import javax.inject.Inject

@PerFragment
class CounterConnectionFactory @Inject constructor(
    private val store: CounterStore
) : DelegateConnectionRulesFactory<CounterFragment>() {

    override val connectionRulesFactory: ConnectionRulesFactory<CounterFragment> = connectionRulesFactory { view ->
        baseRule { store to view }
        autoCancel { store } // magic is here )))
    }
}
