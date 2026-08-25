package com.cedacri.arete.presentation.screens.seatReservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedacri.arete.data.local.datasource.TokenStorage
import com.cedacri.arete.domain.model.office.OfficeGroup
import com.cedacri.arete.domain.model.office.OfficeLayout
import com.cedacri.arete.domain.model.office.Seat
import com.cedacri.arete.domain.repository.SeatReservationRepository
import com.cedacri.arete.domain.usecase.CreateSeatReservationUseCase
import com.cedacri.arete.domain.usecase.GetOfficeGroupsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SeatReservationViewModel(
    private val repository: SeatReservationRepository,
    private val tokenStorage: TokenStorage,
    private val createSeatReservationUseCase: CreateSeatReservationUseCase,
    private val getOfficeGroupsUseCase: GetOfficeGroupsUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            SeatReservationUiState()
        )

    val uiState =
        _uiState.asStateFlow()

    private val _events =
        Channel<SeatReservationEvent>()

    val events =
        _events.receiveAsFlow()

    init {
        loadReservations(
            LocalDate.now()
        )

        loadGroups()
    }

    fun onIntent(
        intent: SeatReservationIntent
    ) {
        when (intent) {
            is SeatReservationIntent.DateChanged -> {
                _uiState.update {
                    it.copy(
                        selectedDate = intent.date
                    )

                }
                loadReservations(intent.date)
                loadGroups()
            }

            is SeatReservationIntent.OfficeChanged -> {
                _uiState.update {
                    it.copy(
                        selectedOfficeId = intent.officeId
                    )
                }
            }

            SeatReservationIntent.Refresh -> {
                loadReservations(
                    _uiState.value.selectedDate
                )
            }

            SeatReservationIntent.CancelReservation -> {
                _uiState.update {
                    it.copy(
                        selectedSeat = null
                    )
                }
            }

            SeatReservationIntent.ConfirmReservation -> {
                reserveSelectedSeat()
            }

            SeatReservationIntent.DismissEmployeeInfo -> {
                _uiState.update {
                    it.copy(
                        selectedEmployee = null
                    )
                }
            }

            is SeatReservationIntent.SeatClicked -> {
                val seat = intent.seat

                if (seat.reservedBy != null) {
                    _uiState.update {
                        it.copy(
                            selectedEmployee =
                                seat.reservedBy
                        )
                    }
                    return
                }

                val group = _uiState.value.currentUserGroup
                val canReserve = seat.active && group?.seatIds?.contains(seat.id) == true

                if (!canReserve) {
                    return
                }

                _uiState.update {
                    it.copy(
                        selectedSeat = seat
                    )
                }
            }

            is SeatReservationIntent.GroupToggled -> {
                _uiState.update { state ->
                    val selectedGroups = state.selectedGroupIds.toMutableSet()

                    if (intent.groupId in selectedGroups) {
                        selectedGroups.remove(intent.groupId)
                    } else {
                        selectedGroups.add(intent.groupId)
                    }

                    state.copy(
                        selectedGroupIds = selectedGroups
                    )
                }
            }
        }
    }

    private fun reserveSelectedSeat() {
        val state = _uiState.value
        val seat = state.selectedSeat ?: return
        if (!seat.active) return
        if (seat.reservedBy != null) return

        viewModelScope.launch {
            val result = createSeatReservationUseCase(
                seatId = seat.id,
                date = state.selectedDate
            )

            result
                .onSuccess { reservationId ->

                    _uiState.update {
                        it.copy(
                            selectedSeat = null,
                            isLoading = false
                        )
                    }

                    _events.send(
                        SeatReservationEvent.ShowMessage(
                            "Seat reserved successfully"
                        )
                    )

                    loadReservations(
                        state.selectedDate
                    )
                }
                .onFailure { error ->

                    _uiState.update {
                        it.copy(
                            selectedSeat = null,
                            isLoading = false
                        )
                    }

                    _events.send(
                        SeatReservationEvent.ShowError(
                            error.message
                                ?: "Failed to reserve seat"
                        )
                    )
                }
        }
    }

    private fun loadReservations(
        date: LocalDate
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            repository
                .getOfficeLayouts(date)
                .onSuccess { offices ->
                    val currentState = _uiState.value
                    val loginId = tokenStorage
                        .getLoginId()
                        ?.lowercase()
                    val currentUserOfficeId =
                        loginId?.let {
                            findCurrentUserOfficeId(
                                offices = offices,
                                loginId = it
                            )
                        }
                    val selectedOfficeId =
                        when {
                            currentState.selectedOfficeId != null &&
                                    offices.any {
                                        it.officeId ==
                                                currentState.selectedOfficeId
                                    } -> {
                                currentState.selectedOfficeId
                            }
                            currentUserOfficeId != null -> {
                                currentUserOfficeId
                            }
                            else -> {
                                offices.firstOrNull()?.officeId
                            }
                        }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            offices = offices,
                            selectedOfficeId = selectedOfficeId
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message
                        )
                    }

                    _events.send(
                        SeatReservationEvent.ShowError(
                            throwable.message
                                ?: "Unknown error"
                        )
                    )
                }
        }
    }

    private fun loadGroups() {
        viewModelScope.launch {
            val loginId =
                tokenStorage.getLoginId()
                    ?.lowercase()

            if (loginId == null) {
                _events.send(
                    SeatReservationEvent.ShowError(
                        "Unable to determine logged in user"
                    )
                )
                return@launch
            }
            getOfficeGroupsUseCase()
                .onSuccess { groups ->
                    val userGroup =
                        groups.firstOrNull { group ->

                            loginId in group.employeeIds
                        }

                    _uiState.update {
                        it.copy(
                            groups = groups,
                            currentUserGroup = userGroup
                        )
                    }
                }
                .onFailure { error ->
                    _events.send(
                        SeatReservationEvent.ShowError(
                            error.message
                                ?: "Failed to load groups"
                        )
                    )
                }
        }
    }

    private fun findCurrentUserOfficeId(
        offices: List<OfficeLayout>,
        loginId: String
    ): Int? {
        return offices.firstOrNull { office ->
            office.seats.any { seat ->
                seat.reservedBy?.id?.equals(
                    loginId,
                    ignoreCase = true
                ) == true
            }
        }?.officeId
    }

    private fun canReserveSeat(
        seat: Seat,
        group: OfficeGroup?
    ): Boolean {

        return seat.active &&
                seat.reservedBy == null &&
                group?.seatIds?.contains(seat.id) == true
    }
}
