package com.cedacri.arete.data.remote.response

data class GroupRequestResponse(
    val id: Int,
    val name: String,
    val color: String,
    val allocatedGroupSeats: Int,
    val seats: List<GroupSeatResponse>,
    val employees: List<String>
)
