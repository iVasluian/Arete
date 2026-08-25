package com.cedacri.arete.presentation.screens.seatReservation

object SeatCoordinateParser {

    fun parse(
        seatName: String
    ): Pair<Int, Int> {

        val coordinates =
            seatName.substringAfter("-")

        val row = coordinates
            .dropLast(1)
            .toInt()

        val letter =
            coordinates.last()

        val column =
            letter.code - 'A'.code + 1

        return row to column

    }

}