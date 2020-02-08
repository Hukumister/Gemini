package com.haroncode.gemini.core

import io.reactivex.disposables.Disposable

/**
 * @author HaronCode.
 */
interface ConnectionBinder : Disposable {

    fun bind(connectionRule: ConnectionRule)
}