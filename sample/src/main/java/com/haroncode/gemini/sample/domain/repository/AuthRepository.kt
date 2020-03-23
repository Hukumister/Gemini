package com.haroncode.gemini.sample.domain.repository

import com.haroncode.gemini.sample.data.MockApi
import com.haroncode.gemini.sample.domain.model.Resource
import com.haroncode.gemini.sample.domain.model.auth.AuthRequest
import com.haroncode.gemini.sample.domain.model.auth.AuthResponse
import com.haroncode.gemini.sample.util.asResource
import io.reactivex.Flowable
import javax.inject.Inject

interface AuthRepository {

    fun auth(login: String, password: String): Flowable<Resource<AuthResponse>>
}

class AuthRepositoryImpl @Inject constructor(
    private val api: MockApi
) : AuthRepository {

    override fun auth(login: String, password: String): Flowable<Resource<AuthResponse>> {
        return api.auth(AuthRequest(login, password)).asResource()
    }

}