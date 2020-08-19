package com.haroncode.gemini.connector

import com.haroncode.gemini.element.Store
import kotlinx.coroutines.cancel

/**
 * @author HaronCode
 * @author kdk96
 */
sealed class ConnectionRule

class AutoCancelStoreRule(
    private val store: Store<*, *, *>
) : ConnectionRule() {

    fun cancel() = store.coroutineScope.cancel()
}

abstract class AbstractConnectionRule : ConnectionRule() {

    abstract suspend fun connect()
}
