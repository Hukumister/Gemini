package com.haroncode.gemini.viewmode.binder

import com.haroncode.gemini.core.Binder

interface ViewBinding<View : Any> {

    fun onCreate(view: View, binder: Binder)
}