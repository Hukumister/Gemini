package com.haroncode.gemini.android.binder

import androidx.lifecycle.LifecycleOwner

interface Binder<View : LifecycleOwner> {

    fun bind(view: View)
}
