package com.haroncode.gemini.viewmode.binder

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.binder.Lifecycle
import com.haroncode.gemini.binder.LifecycleBinder
import io.reactivex.disposables.Disposable

object AndroidBinder {

    fun <T : LifecycleOwner> onView(lifecycleOwner: LifecycleOwner): Builder<T> = TODO()

    fun <T : LifecycleOwner> onBindings(viewBinding: ViewBinding<T>): Builder<T> = Builder(viewBinding)

    class Builder<T : LifecycleOwner>(private val viewBinding: ViewBinding<T>) {

        fun withStrategy(): Builder<T> {
            return this
        }

        fun create(view: T): Disposable {
            val lifecycle = toLifecycle(view)
            val lifecycleBinder = LifecycleBinder(lifecycle)
            val androidLifecycleBinder = AndroidLifecycleBinder(lifecycleBinder)
            viewBinding.onCreate(view, androidLifecycleBinder)
            return androidLifecycleBinder
        }

        private fun toLifecycle(lifecycleOwner: LifecycleOwner): Lifecycle {
            return TODO()
        }

    }

}