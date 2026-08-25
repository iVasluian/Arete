package com.cedacri.arete.presentation.screens.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedacri.arete.data.local.datasource.TokenStorage
import com.cedacri.arete.domain.model.preferences.Appearance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val tokenStorage: TokenStorage
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            PreferencesUiState()
        )

    val uiState =
        _uiState.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {

        viewModelScope.launch {

            val appearance =
                tokenStorage.getAppearance()

            val seatColor =
                tokenStorage.getAvailableSeatColor()

            _uiState.update {

                it.copy(
                    appearance = appearance,
                    availableSeatColor = seatColor
                )
            }
        }
    }

    fun setAppearance(
        appearance: Appearance
    ) {

        viewModelScope.launch {

            tokenStorage.saveAppearance(
                appearance
            )

            _uiState.update {

                it.copy(
                    appearance = appearance
                )
            }
        }
    }

    fun setAvailableSeatColor(
        color: String
    ) {

        viewModelScope.launch {

            tokenStorage.saveAvailableSeatColor(
                color
            )

            _uiState.update {

                it.copy(
                    availableSeatColor = color
                )
            }
        }
    }
}