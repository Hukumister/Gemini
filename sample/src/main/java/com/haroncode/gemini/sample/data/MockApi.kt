package com.haroncode.gemini.sample.data

import com.haroncode.gemini.sample.domain.model.auth.AuthRequest
import com.haroncode.gemini.sample.domain.model.auth.AuthResponse
import com.haroncode.gemini.sample.domain.model.user.UserInfo
import io.reactivex.Single

interface MockApi {

    fun auth(authRequest: AuthRequest): Single<AuthResponse>

    fun getUserInfo(): Single<UserInfo>
}
