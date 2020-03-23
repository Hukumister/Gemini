package com.haroncode.gemini.sample.presentation.onlyaction

import com.haroncode.gemini.android.connection.DelegateConnectionRuleFactory
import com.haroncode.gemini.connection.ConnectionRule
import com.haroncode.gemini.dsl.connectTo
import com.haroncode.gemini.dsl.connectionFactory
import com.haroncode.gemini.dsl.eventsTo
import com.haroncode.gemini.dsl.observeOn
import com.haroncode.gemini.sample.domain.system.SchedulersProvider
import com.haroncode.gemini.sample.ui.AuthFragment
import javax.inject.Inject

class AuthConnectionFactory @Inject constructor(
    private val store: AuthStore,
    private val schedulersProvider: SchedulersProvider
) : DelegateConnectionRuleFactory<AuthFragment>() {

    override val connectionRuleFactory: ConnectionRule.Factory<AuthFragment> = connectionFactory { view ->
        rule { view connectTo store }
        rule { store connectTo view observeOn schedulersProvider.ui() }

        rule { store eventsTo view observeOn schedulersProvider.ui() }
        autoDispose { store } // magic is here )))
    }
}
