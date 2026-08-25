package com.cedacri.arete.data.remote.response

data class OfficeResponse(
    val id: Int,
    val name: String,
    val seatsCapacity: Int,
    val lockersCapacity: Int,
    val columns: Int,
    val metadata: String,
    val deletedAt: String?,
    val deletedBy: String,
    val seats: List<SeatResponse>,
    val lockers: List<LockerResponse>
)