package com.cedacri.arete.presentation.screens.seatReservation

sealed interface SeatReservationEvent {

    data class ShowError(
        val message: String
    ) : SeatReservationEvent

    data class ShowMessage(
        val message: String
    ) : SeatReservationEvent
}