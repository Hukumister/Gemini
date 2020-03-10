package com.haroncode.gemini.android.connection

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.connection.ConnectionRule

abstract class DelegateConnectionRuleFactory<T : LifecycleOwner> : ConnectionRule.Factory<T> {

    abstract val connectionRuleFactory: ConnectionRule.Factory<T>

    override fun create(param: T) = connectionRuleFactory.create(param)
}
