package com.cedacri.arete.domain.model.reservation

data class Reservation(
    val id: Int,
    val employeeId: String,
    val firstName: String,
    val lastName: String,
    val group: String,
    val seatName: String,
    val startDate: String,
    val endDate: String,
    val createdBy: String,
    val deletedBy: String
)