package com.haroncode.gemini.sample.presentation.justreducer

import com.haroncode.gemini.android.connector.DelegateConnectionRulesFactory
import com.haroncode.gemini.android.connector.dsl.connectTo
import com.haroncode.gemini.android.connector.dsl.connectionRulesFactory
import com.haroncode.gemini.android.connector.dsl.stateTo
import com.haroncode.gemini.android.connector.dsl.transform
import com.haroncode.gemini.connector.ConnectionRulesFactory
import com.haroncode.gemini.connector.identityTransformer
import com.haroncode.gemini.sample.ui.CounterFragment
import javax.inject.Inject

class CounterConnectionFactory @Inject constructor(
    private val store: CounterStore
) : DelegateConnectionRulesFactory<CounterFragment>() {

    override val connectionRulesFactory: ConnectionRulesFactory<CounterFragment> = connectionRulesFactory { view ->
        rule { view.actionFlow connectTo store transform identityTransformer() }
        rule { store stateTo view }

        autoCancel { store } // magic is here )))
    }
}
