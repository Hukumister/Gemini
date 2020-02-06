package com.haroncode.gemini.core

import io.reactivex.disposables.Disposable

interface ConnectionRule {

    val isRetain: Boolean

    fun connect(): Disposable

    interface ConnectionsRuleFactory<Param : Any> {

        fun create(param: Param): List<ConnectionRule>
    }
}