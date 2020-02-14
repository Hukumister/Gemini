package com.haroncode.gemini.android.binder.viewmodel

import com.haroncode.gemini.core.ConnectionRule
import com.haroncode.gemini.core.StoreView
import io.reactivex.disposables.Disposable

class ViewModelViewBinder<Action : Any, ViewState : Any>(
    private val connectionFactory: MutableList<ConnectionRule>.(storeView: StoreView<Action, ViewState>) -> Unit
) : ViewBinder<Action, ViewState>, Disposable {

    private val binder = ViewModelConnectionBinder()
    private val setChild = mutableSetOf<ViewBinder<Action, ViewState>>()

    fun addChild(viewModelBinder: ViewBinder<Action, ViewState>) = setChild.add(viewModelBinder)

    override fun bindView(storeView: StoreView<Action, ViewState>) {
        val connectionList = mutableListOf<ConnectionRule>()
        connectionList.connectionFactory(storeView)
        connectionList.forEach(binder::bind)
        setChild.forEach { viewModelBinder -> viewModelBinder.bindView(storeView) }
    }

    override fun unbindView() = binder.clear()

    override fun isDisposed() = binder.isDisposed

    override fun dispose() = binder.dispose()
}