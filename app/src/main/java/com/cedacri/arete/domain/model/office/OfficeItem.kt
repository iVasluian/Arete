package com.cedacri.arete.domain.model.office

sealed interface OfficeItem {
    val row: Int
    val column: Int
    val rowSpan: Int
    val columnSpan: Int
}