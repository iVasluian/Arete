package com.cedacri.arete.presentation.screens.seatReservation.components

import com.cedacri.arete.domain.model.office.Obstacle
import com.cedacri.arete.domain.model.office.Seat
import com.cedacri.arete.presentation.screens.seatReservation.grid.GridContent

data class GridCellData(

    val row: Int,

    val column: Int,

    val content: GridContent

)