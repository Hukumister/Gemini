package com.haroncode.gemini.viewmode.binder

import com.haroncode.gemini.core.Binder

typealias DelegateBinding<View> = (View, Binder) -> Unit

abstract class DelegateViewBinding<View : Any> : ViewBinding<View> {

    abstract val binding: DelegateBinding<View>

    final override fun onCreate(view: View, binder: Binder) = binding.invoke(view, binder)
}