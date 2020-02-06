package com.haroncode.gemini.binder

import com.haroncode.gemini.binder.Lifecycle.State
import com.haroncode.gemini.core.Binder
import com.haroncode.gemini.core.ConnectionRule
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class LifecycleBinder(lifecycle: Lifecycle) : Binder {

    private val connectionRuleDisposables = CompositeDisposable()
    private val retainRuleDisposables = CompositeDisposable()
    private val lifecycleDisposables = CompositeDisposable(connectionRuleDisposables, retainRuleDisposables)

    private val connectionRules = mutableSetOf<ConnectionRule>()
    private var isActive: Boolean = false

    init {
        Flowable.fromPublisher(lifecycle)
            .distinctUntilChanged()
            .subscribe { state ->
                when (state) {
                    State.START -> connectAll()
                    State.STOP -> disconnectAll()
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