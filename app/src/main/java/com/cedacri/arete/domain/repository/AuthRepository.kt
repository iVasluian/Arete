package com.cedacri.arete.domain.repository

import com.cedacri.arete.domain.model.login.LoginResponse

interface AuthRepository {
    suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse>
}