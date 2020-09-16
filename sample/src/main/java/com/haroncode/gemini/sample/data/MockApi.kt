package com.haroncode.gemini.sample.data

import com.haroncode.gemini.sample.domain.model.auth.AuthRequest
import com.haroncode.gemini.sample.domain.model.auth.AuthResponse

interface MockApi {

    suspend fun auth(authRequest: AuthRequest): AuthResponse
}
