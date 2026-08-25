package com.cedacri.arete.presentation.screens.seatReservation.grid

import com.cedacri.arete.domain.model.office.Obstacle
import com.cedacri.arete.domain.model.office.Seat

sealed interface GridContent {

    /**
     * Empty floor.
     */
    data object Empty : GridContent

    /**
     * Do not render anything.
     *
     * Used by cells already occupied by a rowSpan/columnSpan.
     */
    data object Skip : GridContent

    /**
     * Seat.
     */
    data class SeatContent(
        val seat: Seat
    ) : GridContent

    /**
     * Obstacle.
     */
    data class ObstacleContent(
        val obstacle: Obstacle
    ) : GridContent

}