package com.cedacri.arete.presentation.screens.login

sealed interface LoginIntent {

    data class UsernameChanged(val username: String) : LoginIntent

    data class PasswordChanged(val password: String) : LoginIntent

    data object TogglePasswordVisibility : LoginIntent

    data object LoginClicked : LoginIntent
}