package com.cedacri.arete.domain.model.office

data class Seat(
    val id: Int,
    val name: String,
    override val row: Int,
    override val column: Int,
    override val rowSpan: Int = 1,
    override val columnSpan: Int = 1,
    val active: Boolean,
    val reservedBy: Employee?
) : OfficeItem {

    val isReserved: Boolean
        get() = reservedBy != null

}