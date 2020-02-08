package com.haroncode.gemini.android.binder

import android.os.Looper
import com.haroncode.gemini.core.Binder
import com.haroncode.gemini.core.ConnectionRule

class AndroidLifecycleBinder(
    private val binder: Binder
) : Binder {

    override fun bind(connectionRule: ConnectionRule) {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method bind() can invoked only in main thread" }
        binder.bind(connectionRule)
    }

    override fun dispose() {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method dispose() can invoked only in main thread" }
        binder.dispose()
    }

    override fun isDisposed(): Boolean {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method isDisposed() can invoked only in main thread" }
        return binder.isDisposed
    }
}