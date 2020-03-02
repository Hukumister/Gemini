package com.haroncode.gemini.connection

import io.reactivex.disposables.Disposable

/**
 * @author HaronCode.
 */
interface ConnectionRule {

    val isRetain: Boolean

    fun connect(): Disposable

    interface Factory<P : Any> {

        fun create(param: P): Collection<ConnectionRule>
    }
}
