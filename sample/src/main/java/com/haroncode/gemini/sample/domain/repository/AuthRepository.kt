package com.haroncode.gemini.sample.domain.repository

import com.haroncode.gemini.sample.data.MockApi
import com.haroncode.gemini.sample.domain.model.Resource
import com.haroncode.gemini.sample.domain.model.auth.AuthRequest
import com.haroncode.gemini.sample.domain.model.auth.AuthResponse
import com.haroncode.gemini.sample.util.asResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface AuthRepository {

    fun auth(login: String, password: String): Flow<Resource<AuthResponse>>
}

class AuthRepositoryImpl @Inject constructor(
    private val api: MockApi,
) : AuthRepository {

    override fun auth(login: String, password: String): Flow<Resource<AuthResponse>> {
        return flow { emit(api.auth(AuthRequest(login, password))) }
            .flowOn(Dispatchers.IO)
            .asResource()
    }
}
