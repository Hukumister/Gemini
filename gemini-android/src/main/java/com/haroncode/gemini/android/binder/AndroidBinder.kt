package com.haroncode.gemini.android.binder

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.binder.BaseConnectionBinder
import com.haroncode.gemini.android.binder.strategies.StartStopStrategy
import io.reactivex.disposables.Disposable

object AndroidBinder {

    fun <T : LifecycleOwner> withBinding(viewBinding: ViewBinding<T>): Builder<T> = Builder(viewBinding)

    class Builder<T : LifecycleOwner>(private val viewBinding: ViewBinding<T>) {

        private var bindingStrategy: BindingStrategy = StartStopStrategy

        fun withStrategy(bindingStrategy: BindingStrategy): Builder<T> {
            this.bindingStrategy = bindingStrategy
            return this
        }

        fun create(view: T): Disposable {
            val lifecycle = AndroidStoreLifecycle(view.lifecycle, bindingStrategy)
            val lifecycleBinder = BaseConnectionBinder(lifecycle)
            val androidLifecycleBinder = AndroidConnectionBinder(lifecycleBinder)
            viewBinding.onCreate(view, androidLifecycleBinder)
            return androidLifecycleBinder
        }
    }
}