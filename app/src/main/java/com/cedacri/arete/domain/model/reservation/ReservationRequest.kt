package com.cedacri.arete.domain.model.reservation

data class ReservationRequest(
    val seatId: Int,
    val date: String
)