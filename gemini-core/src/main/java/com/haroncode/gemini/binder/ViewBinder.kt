package com.haroncode.gemini.binder

import com.haroncode.gemini.core.StoreView

interface ViewBinder<Action : Any, State : Any> {

    fun bindView(storeView: StoreView<Action, State>)

    fun unbindView()
}
