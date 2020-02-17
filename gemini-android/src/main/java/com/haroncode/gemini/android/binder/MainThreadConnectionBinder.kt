package com.haroncode.gemini.android.binder

import android.os.Looper
import com.haroncode.gemini.core.ConnectionBinder
import com.haroncode.gemini.core.ConnectionRule

class MainThreadConnectionBinder(
    private val connectionBinder: ConnectionBinder
) : ConnectionBinder {

    override fun bind(connectionRule: ConnectionRule) {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method bind() should be invoked only on main thread" }
        connectionBinder.bind(connectionRule)
    }

    override fun dispose() {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method dispose() should be invoked only on main thread" }
        connectionBinder.dispose()
    }

    override fun isDisposed(): Boolean {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method isDisposed() should be invoked only on main thread" }
        return connectionBinder.isDisposed
    }
}