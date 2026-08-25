package com.cedacri.arete.presentation.screens.reservations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedacri.arete.domain.usecase.GetReservationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReservationsViewModel(
    private val getReservationsUseCase: GetReservationsUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            ReservationsUiState()
        )

    val uiState =
        _uiState.asStateFlow()

    init {
        loadReservations()
    }

    fun onIntent(
        intent: ReservationsIntent
    ) {
        when (intent) {

            is ReservationsIntent.SearchChanged -> {

                _uiState.update {
                    it.copy(
                        searchQuery = intent.query
                    )
                }
            }

            is ReservationsIntent.StartDateChanged -> {

                _uiState.update {
                    it.copy(
                        startDate = intent.date
                    )
                }

                loadReservations()
            }

            is ReservationsIntent.EndDateChanged -> {

                _uiState.update {
                    it.copy(
                        endDate = intent.date
                    )
                }

                loadReservations()
            }

            is ReservationsIntent.StatusChanged -> {

                _uiState.update {
                    it.copy(
                        status = intent.status
                    )
                }

                loadReservations()
            }

            ReservationsIntent.Refresh -> {
                loadReservations()
            }
        }
    }

    private fun loadReservations() {

        val state = _uiState.value

        if (state.startDate.isAfter(state.endDate)) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            getReservationsUseCase(
                startDate = state.startDate,
                endDate = state.endDate,
                status = state.status
            )
                .onSuccess { reservations ->

                    _uiState.update {
                        it.copy(
                            reservations =
                                reservations,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error =
                                error.message
                        )
                    }
                }
        }
    }
}