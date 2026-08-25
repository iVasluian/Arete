package com.cedacri.arete.data.remote.response

data class ReservationsRangeResponse(
    val officeId: Int,
    val reservations: List<ReservationRangeResponse>
)