package com.haroncode.gemini.core

import io.reactivex.disposables.Disposable

interface Binder : Disposable {

    fun bind(connectionRule: ConnectionRule)
}