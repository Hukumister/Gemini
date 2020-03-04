package com.haroncode.gemini.dsl

import com.haroncode.gemini.connection.AutoStoreDisposeConnectionRule
import com.haroncode.gemini.connection.ConnectionRule
import com.haroncode.gemini.core.Store

/**
 * @author HaronCode.
 */
inline fun <reified T : Any> connectionFactory(
    crossinline factory: MutableList<ConnectionRule>.(param: T) -> Unit
): ConnectionRule.Factory<T> = object : ConnectionRule.Factory<T> {

    override fun create(param: T): Collection<ConnectionRule> {
        val mutableList = mutableListOf<ConnectionRule>()
        mutableList.factory(param)
        return mutableList
    }
}

inline fun MutableList<ConnectionRule>.connect(connectionRule: () -> ConnectionRule) = connectionRule
    .invoke()
    .let(::add)

inline fun MutableList<ConnectionRule>.autoDispose(storeFactory: () -> Store<*, *, *>) = storeFactory.invoke()
    .let(::AutoStoreDisposeConnectionRule)
    .let(::add)