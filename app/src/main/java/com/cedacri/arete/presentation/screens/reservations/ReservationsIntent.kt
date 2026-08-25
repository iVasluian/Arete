package com.cedacri.arete.presentation.screens.reservations

import com.cedacri.arete.domain.model.reservation.ReservationStatus
import java.time.LocalDate

sealed interface ReservationsIntent {

    data class SearchChanged(
        val query: String
    ) : ReservationsIntent

    data class StartDateChanged(
        val date: LocalDate
    ) : ReservationsIntent

    data class EndDateChanged(
        val date: LocalDate
    ) : ReservationsIntent

    data class StatusChanged(
        val status: ReservationStatus
    ) : ReservationsIntent

    data object Refresh : ReservationsIntent
}