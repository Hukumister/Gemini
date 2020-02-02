package com.haroncode.gemini.connection

import io.reactivex.disposables.Disposable

interface ConnectionRule {

    fun connect(): Disposable

}