package com.haroncode.gemini.connector

import io.reactivex.disposables.Disposable

/**
 * @author HaronCode.
 */
interface StoreConnector : Disposable {

    fun connect()

    fun disconnect()
}