package com.haroncode.gemini.sample.data

import com.haroncode.gemini.sample.domain.model.auth.AuthRequest
import com.haroncode.gemini.sample.domain.model.auth.AuthResponse
import io.reactivex.Single

interface MockApi {

    fun auth(authRequest: AuthRequest): Single<AuthResponse>
}
