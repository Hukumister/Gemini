package com.haroncode.gemini.viewmode.binder

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.binder.LifecycleBinder
import com.haroncode.gemini.core.Binder
import com.haroncode.gemini.core.ConnectionRule

class AndroidLifecycleBinder(
    private val lifecycleBinder: LifecycleBinder
) : Binder {

    override fun bind(connectionRule: ConnectionRule) {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method bind() can invoked only in main thread" }
        lifecycleBinder.bind(connectionRule)
    }

    override fun dispose() {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method dispose() can invoked only in main thread" }
        lifecycleBinder.dispose()
    }

    override fun isDisposed(): Boolean {
        check(Looper.myLooper() == Looper.getMainLooper()) { "Method isDisposed() can invoked only in main thread" }
        return lifecycleBinder.isDisposed
    }

    companion object {

        fun ofLifecycleOwner(lifecycleOwner: LifecycleOwner): AndroidLifecycleBinder {
            val binder = LifecycleBinder(lifecycle)
            return AndroidLifecycleBinder(binder)
        }
    }
}