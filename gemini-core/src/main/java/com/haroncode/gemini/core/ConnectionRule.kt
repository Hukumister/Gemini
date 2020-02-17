package com.haroncode.gemini.core

import io.reactivex.disposables.Disposable

/**
 * @author HaronCode.
 */
interface ConnectionRule {

    val isRetain: Boolean

    fun connect(): Disposable
}