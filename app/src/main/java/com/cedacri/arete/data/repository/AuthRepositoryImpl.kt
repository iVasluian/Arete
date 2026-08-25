package com.cedacri.arete.data.repository

import com.cedacri.arete.data.local.datasource.TokenStorage
import com.cedacri.arete.data.remote.api.AuthApi
import com.cedacri.arete.domain.model.login.LoginRequest
import com.cedacri.arete.domain.model.login.LoginResponse
import com.cedacri.arete.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse> {

        return try {

            val response = api.login(
                LoginRequest(
                    username = username,
                    password = password
                )
            )

            tokenStorage.saveLoginData(response)

            Result.success(response)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}