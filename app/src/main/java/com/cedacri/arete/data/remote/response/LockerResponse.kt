package com.cedacri.arete.data.remote.response

data class LockerResponse(
    val id: Int,
    val name: String,
    val employeeId: String?,
    val employee: EmployeeResponse?,
    val updatedAt: String?,
    val updatedBy: String?
)