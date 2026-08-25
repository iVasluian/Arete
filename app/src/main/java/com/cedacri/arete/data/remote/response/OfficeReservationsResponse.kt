package com.cedacri.arete.data.remote.response

data class OfficeReservationsResponse(
    val officeId: Int,
    val reservations: List<ReservationResponse>
)