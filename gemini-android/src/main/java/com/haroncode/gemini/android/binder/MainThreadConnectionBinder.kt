package com.haroncode.gemini.android.binder

import android.os.Looper
import com.haroncode.gemini.core.ConnectionBinder
import com.haroncode.gemini.core.ConnectionRule

class MainThreadConnectionBinder(
    private val connectionBinder: ConnectionBinder
) : ConnectionBinder {

    override fun bind(connectionRule: ConnectionRule) {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method bind() can invoked only in main thread" }
        connectionBinder.bind(connectionRule)
    }

    override fun dispose() {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method dispose() can invoked only in main thread" }
        connectionBinder.dispose()
    }

    override fun isDisposed(): Boolean {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method isDisposed() can invoked only in main thread" }
        return connectionBinder.isDisposed
    }
}