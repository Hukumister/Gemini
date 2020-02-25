package com.haroncode.gemini.binder

import com.haroncode.gemini.core.ConnectionBinder
import com.haroncode.gemini.core.ConnectionRule
import com.haroncode.gemini.core.StoreLifecycle
import com.haroncode.gemini.core.StoreLifecycle.Event
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class BaseConnectionBinder(storeLifecycle: StoreLifecycle) : ConnectionBinder {

    private val connectionRuleDisposables = CompositeDisposable()
    private val retainRuleDisposables = CompositeDisposable()
    private val lifecycleDisposables = CompositeDisposable(connectionRuleDisposables, retainRuleDisposables)

    private val connectionRules = mutableSetOf<ConnectionRule>()
    private var isActive: Boolean = false

    init {
        Flowable.fromPublisher(storeLifecycle)
            .distinctUntilChanged()
            .subscribe { state ->
                when (state) {
                    Event.START -> connectAll()
                    Event.STOP -> disconnectAll()
                }
            }.addTo(lifecycleDisposables)
    }

    override fun bind(connectionRule: ConnectionRule) {
        connectionRules.add(connectionRule)
        connectRule(connectionRule)
    }

    override fun isDisposed(): Boolean = lifecycleDisposables.isDisposed

    override fun dispose() {
        connectionRules.clear()
        lifecycleDisposables.dispose()
    }

    private fun connectRule(connectionRule: ConnectionRule) {
        when {
            isActive && connectionRule.isRetain -> connectionRule.connect().addTo(retainRuleDisposables)
            isActive -> connectionRule.connect().addTo(connectionRuleDisposables)
        }
    }

    private fun connectAll() {
        isActive = true
        connectionRules.filterNot(ConnectionRule::isRetain).forEach(::connectRule)
    }

    private fun disconnectAll() {
        isActive = false
        connectionRuleDisposables.clear()
    }
}
