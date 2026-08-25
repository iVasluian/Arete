package com.cedacri.arete.presentation.screens.lockers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedacri.arete.domain.repository.SeatReservationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LockersViewModel(
    private val repository: SeatReservationRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            LockersUiState(
                isLoading = true
            )
        )

    val uiState =
        _uiState.asStateFlow()

    init {
        loadLockers()
    }

    private fun loadLockers() {

        viewModelScope.launch {

            repository
                .getLockers()
                .onSuccess { data ->

                    val selectedOfficeId =
                        _uiState.value.selectedOfficeId
                            ?: data.offices.firstOrNull()?.id

                    _uiState.update {
                        it.copy(
                            lockers = data.lockers,
                            offices = data.offices,
                            selectedOfficeId =
                                selectedOfficeId,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun selectOffice(
        officeId: Int?
    ) {

        _uiState.update {
            it.copy(
                selectedOfficeId = officeId
            )
        }
    }

    fun refresh() {
        loadLockers()
    }
}