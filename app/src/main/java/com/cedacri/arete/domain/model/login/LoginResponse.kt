package com.cedacri.arete.domain.model.login

data class LoginResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: Int,
    val authToken: String,
    val preferences: String
)
