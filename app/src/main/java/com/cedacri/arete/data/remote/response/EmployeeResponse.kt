package com.cedacri.arete.data.remote.response

data class EmployeeResponse(
    val id: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val holidayCountry: String?,
    val role: Int?,
    val group: GroupResponse?
)