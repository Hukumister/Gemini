package com.haroncode.gemini.binder

import com.haroncode.gemini.core.StoreView
import java.util.concurrent.CopyOnWriteArraySet

class BinderComposer<Action : Any, State : Any>(
    vararg binders: ViewBinder<Action, State>
) : ViewBinder<Action, State> {

    private val binderSet = CopyOnWriteArraySet<ViewBinder<Action, State>>().apply { addAll(binders) }

    override fun bindView(storeView: StoreView<Action, State>) =
        binderSet.forEach { binder -> binder.bindView(storeView) }

    override fun unbindView() = binderSet.forEach { binder -> binder.unbindView() }

    fun addBinder(binder: ViewBinder<Action, State>) = binderSet.add(binder)

}