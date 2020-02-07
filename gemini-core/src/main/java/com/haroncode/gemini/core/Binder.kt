package com.haroncode.gemini.core

import io.reactivex.disposables.Disposable

/**
 * @author HaronCode.
 */
interface Binder : Disposable {

    fun bind(connectionRule: ConnectionRule)
}