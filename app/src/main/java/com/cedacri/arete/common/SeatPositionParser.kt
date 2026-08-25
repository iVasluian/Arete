package com.cedacri.arete.common

data class SeatPosition(
    val row: Int,
    val column: Int
)

private val seatRegex =
    Regex(""".*-(\d+)([A-Za-z])$""")

fun parseSeatPosition(
    seatName: String
): SeatPosition? {

    val match =
        seatRegex.matchEntire(seatName)
            ?: return null

    val row =
        match.groupValues[1].toIntOrNull()
            ?: return null

    val column =
        match.groupValues[2]
            .uppercase()
            .first() - 'A'

    return SeatPosition(
        row,
        column
    )
}