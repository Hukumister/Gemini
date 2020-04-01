package com.haroncode.gemini.sample.domain.repository

import io.reactivex.Flowable

interface ConnectivityRepository {
    fun observeConnectionState(): Flowable<Boolean>
}
