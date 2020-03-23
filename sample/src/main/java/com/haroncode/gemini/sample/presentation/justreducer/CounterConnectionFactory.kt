package com.haroncode.gemini.sample.presentation.justreducer

import com.haroncode.gemini.android.connection.DelegateConnectionRuleFactory
import com.haroncode.gemini.connection.ConnectionRule
import com.haroncode.gemini.dsl.connectTo
import com.haroncode.gemini.dsl.connectionFactory
import com.haroncode.gemini.dsl.observeOn
import com.haroncode.gemini.sample.domain.system.SchedulersProvider
import com.haroncode.gemini.sample.ui.CounterFragment
import javax.inject.Inject

class CounterConnectionFactory @Inject constructor(
    private val store: CounterStore,
    private val schedulersProvider: SchedulersProvider
) : DelegateConnectionRuleFactory<CounterFragment>() {

    override val connectionRuleFactory: ConnectionRule.Factory<CounterFragment> = connectionFactory { view ->
        rule { view connectTo store }
        rule { store connectTo view observeOn schedulersProvider.ui() }

        autoDispose { store } // magic is here )))
    }
}
