package com.haroncode.gemini.binder

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import com.haroncode.gemini.connection.ConnectionRule
import com.haroncode.gemini.core.StoreView

class ConnectionViewBinder<Action : Any, State : Any>(
    private val connectionFactory: (StoreView<Action, State>) -> List<ConnectionRule>
) : ViewBinder<Action, State> {

    private var childBinder: ConnectionViewBinder<Action, State>? = null
    private var connectionDisposable = CompositeDisposable()

    override fun bindView(storeView: StoreView<Action, State>) {
        val connectionRuleList = connectionFactory.invoke(storeView)
        connectionRuleList.forEach { connectionRule -> connectionRule.connect().addTo(connectionDisposable) }

        childBinder?.bindView(storeView)
    }

    fun addChild(child: ConnectionViewBinder<Action, State>) {
        childBinder = child
    }

    override fun unbindView() {
        childBinder?.unbindView()
        connectionDisposable.dispose()
    }

}