package com.cedacri.arete.data.remote.response

data class SeatResponse(
    val id: Int,
    val name: String,
    val type: Int,
    val active: Boolean
)