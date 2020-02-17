package com.haroncode.gemini.android

import com.haroncode.gemini.core.ConnectionBinder

interface ViewBinding<View : Any> {

    fun onCreate(view: View, binder: ConnectionBinder)
}