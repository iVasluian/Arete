package com.cedacri.arete.presentation.screens.seatReservation

import com.cedacri.arete.domain.model.office.Seat
import java.time.LocalDate

sealed interface SeatReservationIntent {

    /**
     * User selected another office.
     */
    data class OfficeChanged(
        val officeId: Int
    ) : SeatReservationIntent

    /**
     * User selected another day.
     */
    data class DateChanged(
        val date: LocalDate
    ) : SeatReservationIntent

    /**
     * Pull to refresh / retry.
     */
    data object Refresh : SeatReservationIntent

    /**
     * User tapped a seat.
     */
    data class SeatClicked(
        val seat: Seat
    ) : SeatReservationIntent

    data class GroupToggled(
        val groupId: Int
    ) : SeatReservationIntent

    data object CancelReservation : SeatReservationIntent
    data object ConfirmReservation : SeatReservationIntent
    data object DismissEmployeeInfo : SeatReservationIntent
}