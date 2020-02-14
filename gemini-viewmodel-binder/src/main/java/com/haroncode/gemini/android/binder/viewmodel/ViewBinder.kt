package com.haroncode.gemini.android.binder.viewmodel

import com.haroncode.gemini.core.StoreView

interface ViewBinder<Action : Any, ViewState : Any> {

    fun bindView(storeView: StoreView<Action, ViewState>)

    fun unbindView()
}