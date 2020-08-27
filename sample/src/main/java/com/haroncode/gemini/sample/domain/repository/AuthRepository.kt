package com.haroncode.gemini.sample.domain.repository

import com.haroncode.gemini.sample.domain.model.Resource
import com.haroncode.gemini.sample.domain.model.auth.AuthResponse
import com.haroncode.gemini.sample.util.asResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

interface AuthRepository {

    fun auth(login: String, password: String): Flow<Resource<AuthResponse>>
}

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override fun auth(login: String, password: String): Flow<Resource<AuthResponse>> {
        return flow { emit(AuthResponse("some_token")) }
            .onStart { delay(3_000) }
            .flowOn(Dispatchers.IO)
            .asResource()
    }
}
