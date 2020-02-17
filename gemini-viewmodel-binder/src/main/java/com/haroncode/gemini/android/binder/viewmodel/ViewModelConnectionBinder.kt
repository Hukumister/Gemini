package com.haroncode.gemini.android.binder.viewmodel

import com.haroncode.gemini.core.ConnectionBinder
import com.haroncode.gemini.core.ConnectionRule
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ViewModelConnectionBinder : ConnectionBinder {

    private val connections = CompositeDisposable()
    private val retainConnections = CompositeDisposable()

    override fun bind(connectionRule: ConnectionRule) {
        if (connectionRule.isRetain) {
            connectionRule.connect().addTo(retainConnections)
        } else {
            connectionRule.connect().addTo(connections)
        }
    }

    fun clear() = connections.clear()

    override fun isDisposed() = connections.isDisposed

    override fun dispose() {
        connections.dispose()
        retainConnections.dispose()
    }
}