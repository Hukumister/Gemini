package com.haroncode.gemini.connector

import com.haroncode.gemini.connection.ConnectionRule
import io.reactivex.disposables.CompositeDisposable

/**
 * @author kdk96.
 * @author HaronCode.
 */
open class BaseStoreConnector(
    private val connectionRules: Collection<ConnectionRule>
) : StoreConnector {

    private val connectionRuleDisposables = CompositeDisposable()
    private val retainRuleDisposables = CompositeDisposable()
    private val lifecycleDisposables = CompositeDisposable(connectionRuleDisposables, retainRuleDisposables)

    private var isNotConnectedRetain = true

    override fun connect() {
        if (isNotConnectedRetain) {
            connectionRules
                .filter(ConnectionRule::isRetain)
                .map(ConnectionRule::connect)
                .forEach { disposable -> retainRuleDisposables.add(disposable) }
            isNotConnectedRetain = false
        }

        connectionRules
            .filterNot(ConnectionRule::isRetain)
            .map(ConnectionRule::connect)
            .forEach { disposable -> connectionRuleDisposables.add(disposable) }
    }

    override fun disconnect() = connectionRuleDisposables.clear()

    override fun isDisposed() = lifecycleDisposables.isDisposed

    override fun dispose() = lifecycleDisposables.dispose()
}
