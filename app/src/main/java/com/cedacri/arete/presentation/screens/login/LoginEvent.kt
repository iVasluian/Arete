package com.cedacri.arete.presentation.screens.login

sealed interface LoginEvent {
    data class ShowError(val message: String) : LoginEvent
}