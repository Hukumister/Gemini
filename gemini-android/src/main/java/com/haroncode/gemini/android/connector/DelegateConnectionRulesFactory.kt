package com.haroncode.gemini.android.connector

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.connector.ConnectionRule
import com.haroncode.gemini.connector.ConnectionRulesFactory

/**
 * @author HaronCode
 * @author kdk96
 */
abstract class DelegateConnectionRulesFactory<T : LifecycleOwner> : ConnectionRulesFactory<T> {

    abstract val connectionRulesFactory: ConnectionRulesFactory<T>

    override fun create(param: T): Collection<ConnectionRule> = connectionRulesFactory.create(param)
}
