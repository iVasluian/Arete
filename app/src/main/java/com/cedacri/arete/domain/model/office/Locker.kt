package com.cedacri.arete.domain.model.office

data class Locker(
    val id: Int,
    val name: String,
    val officeId: Int,
    val officeName: String,
    val lockersCapacity: Int,
    val employeeId: String?,
    val employeeName: String?,
    val updatedAt: String?,
    val updatedBy: String?
)