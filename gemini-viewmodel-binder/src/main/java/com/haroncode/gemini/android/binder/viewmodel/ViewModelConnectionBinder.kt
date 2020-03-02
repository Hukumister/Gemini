package com.haroncode.gemini.android.binder.viewmodel

import com.haroncode.gemini.core.ConnectionRule
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

class ViewModelConnectionBinder : Disposable {

    private val connections = CompositeDisposable()

    fun bind(connectionRule: ConnectionRule) {
        connectionRule.connect().addTo(connections)
    }

    fun clear() = connections.clear()

    override fun isDisposed() = connections.isDisposed

    override fun dispose() = connections.dispose()
}
