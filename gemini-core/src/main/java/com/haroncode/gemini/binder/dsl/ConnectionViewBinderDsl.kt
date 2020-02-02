package com.haroncode.gemini.binder.dsl

import com.haroncode.gemini.binder.ConnectionViewBinder
import com.haroncode.gemini.connection.ConnectionRule
import com.haroncode.gemini.core.StoreView

fun <Action : Any, State : Any> binding(
    listener: MutableList<ConnectionRule>.(storeView: StoreView<Action, State>) -> Unit
): ConnectionViewBinder<Action, State> {
    val connectionViewBindingBuilder = mutableListOf<ConnectionRule>()

    return ConnectionViewBinder { storeView ->
        connectionViewBindingBuilder.apply { listener(storeView) }
    }
}

inline fun MutableList<ConnectionRule>.connection(rule: () -> ConnectionRule) {
    rule.invoke().let(::add)
}