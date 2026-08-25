package com.cedacri.arete.data.remote.response

data class ReservationRangeResponse(
    val id: Int,
    val seatId: Int,
    val seatNameMemo: String,
    val employeeId: String,
    val startDate: String,
    val endDate: String,
    val createdAt: String,
    val createdBy: String,
    val deletedAt: String?,
    val deletedBy: String
)
