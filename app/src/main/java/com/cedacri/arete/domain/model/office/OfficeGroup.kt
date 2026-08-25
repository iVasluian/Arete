package com.cedacri.arete.domain.model.office

data class OfficeGroup(
    val id: Int,
    val name: String,
    val color: String,
    val seatIds: Set<Int>,
    val employeeIds: Set<String>
)
