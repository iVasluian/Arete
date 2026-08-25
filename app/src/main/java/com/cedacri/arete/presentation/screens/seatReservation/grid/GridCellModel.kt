package com.cedacri.arete.presentation.screens.seatReservation.grid
/**
 * Represents one logical cell of the rendered office.
 *
 * The renderer never searches through OfficeLayout.
 * It only renders these cells.
 */
data class GridCellModel(
    val row: Int,
    val column: Int,
    val content: GridContent
)