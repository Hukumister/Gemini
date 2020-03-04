package com.haroncode.gemini.connection

import com.haroncode.gemini.core.Store

/**
 * @author HaronCode.
 */
class AutoStoreDisposeConnectionRule(
    private val store: Store<*, *, *>
) : ConnectionRule {

    override val isRetain: Boolean = true

    override fun connect() = store
}
