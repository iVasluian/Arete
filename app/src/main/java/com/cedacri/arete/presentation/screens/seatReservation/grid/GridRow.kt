package com.cedacri.arete.presentation.screens.seatReservation.grid

data class GridRow(
    val index: Int,
    val displayIndex: Int?,
    val cells: List<GridCellModel>
) {
    val showRowHeader: Boolean
        get() = cells.any { it.content is GridContent.SeatContent }
}