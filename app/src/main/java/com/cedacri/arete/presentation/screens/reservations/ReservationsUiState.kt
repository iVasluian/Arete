package com.cedacri.arete.presentation.screens.reservations

import com.cedacri.arete.domain.model.reservation.Reservation
import com.cedacri.arete.domain.model.reservation.ReservationStatus
import java.time.LocalDate

data class ReservationsUiState(
    val reservations: List<Reservation> = emptyList(),
    val startDate: LocalDate =
        LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate =
        LocalDate.now().withDayOfMonth(
            LocalDate.now().lengthOfMonth()
        ),
    val status: ReservationStatus =
        ReservationStatus.ALL,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)