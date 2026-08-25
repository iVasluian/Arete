package com.cedacri.arete.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedacri.arete.domain.repository.AuthRepository
import com.cedacri.arete.presentation.navigation.NavigationManager
import com.cedacri.arete.presentation.navigation.OfficeMap
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository,
    private val navigation: NavigationManager
) : ViewModel() {

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: LoginIntent) {

        when (intent) {

            is LoginIntent.UsernameChanged -> {
                _uiState.update {
                    it.copy(username = intent.username)
                }
            }

            is LoginIntent.PasswordChanged -> {
                _uiState.update {
                    it.copy(password = intent.password)
                }
            }

            LoginIntent.TogglePasswordVisibility -> {
                _uiState.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            LoginIntent.LoginClicked -> login()
        }
    }

    private fun login() {

        val state = _uiState.value

        val username = state.username
        val password = state.password

        // validation

        if (username.isBlank()) return
        if (password.isBlank()) return

        viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true)
            }

            val result = repository.login(
                state.username,
                state.password
            )

            _uiState.update {
                it.copy(isLoading = false)
            }

            result
                .onSuccess {
                    navigation.navigateTo(OfficeMap)
                }
                .onFailure {
                    _events.send(
                        LoginEvent.ShowError(
                            it.message ?: "Login failed"
                        )
                    )
                }
        }
    }
}