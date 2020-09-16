package com.haroncode.gemini.sample.domain.repository

import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {
    fun observeConnectionState(): Flow<Boolean>
}
