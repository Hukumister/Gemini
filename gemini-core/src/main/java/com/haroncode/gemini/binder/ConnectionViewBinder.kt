package com.haroncode.gemini.binder

import com.haroncode.gemini.core.ConnectionRule
import com.haroncode.gemini.core.StoreView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ConnectionViewBinder<Action : Any, State : Any>(
    private val connectionFactory: (StoreView<Action, State>) -> List<ConnectionRule>
) : ViewBinder<Action, State> {

    private val connectionDisposable = CompositeDisposable()

    override fun bindView(storeView: StoreView<Action, State>) {
        val connectionRuleList = connectionFactory.invoke(storeView)
        connectionRuleList.forEach { connectionRule -> connectionRule.connect().addTo(connectionDisposable) }
    }

    override fun unbindView() = connectionDisposable.dispose()

}