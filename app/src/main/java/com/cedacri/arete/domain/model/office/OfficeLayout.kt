package com.cedacri.arete.domain.model.office

data class OfficeLayout(
    val officeId: Int,
    val officeName: String,
    val columns: Int,
    val seats: List<Seat>,
    val obstacles: List<Obstacle>
) {

    val rows: Int
        get() {
            val maxSeatRow =
                seats.maxOfOrNull { it.row } ?: 0

            val maxObstacleRow =
                obstacles.maxOfOrNull {
                    it.row + it.rowSpan - 1
                } ?: 0

            return maxOf(
                maxSeatRow,
                maxObstacleRow
            )
        }

    val officeTitle: String
        get() = officeName

    /**
     * Seat lookup
     */
    private val seatsMap =
        seats.associateBy {
            it.row to it.column
        }

    /**
     * Every cell occupied by an obstacle.
     */
    private val obstacleMap =
        buildMap<Pair<Int, Int>, Obstacle> {

            obstacles.forEach { obstacle ->

                for (row in obstacle.row until obstacle.row + obstacle.rowSpan) {

                    for (column in obstacle.column until obstacle.column + obstacle.columnSpan) {

                        put(
                            row to column,
                            obstacle
                        )

                    }

                }

            }

        }

    /**
     * Only obstacle origins.
     *
     * Locker (5,1)
     * occupying 5,1-5,4
     *
     * will exist ONLY at (5,1)
     */
    private val obstacleStartMap =
        obstacles.associateBy {

            it.row to it.column

        }

    fun seatAt(
        row: Int,
        column: Int
    ): Seat? {

        return seatsMap[
            row to column
        ]

    }

    fun isCoveredByObstacle(
        row: Int,
        column: Int
    ): Boolean {

        val obstacle =
            obstacleAt(row, column)
                ?: return false

        return obstacle.row != row
                ||
                obstacle.column != column

    }

    /**
     * Returns the obstacle occupying this cell.
     */
    fun obstacleAt(
        row: Int,
        column: Int
    ): Obstacle? {

        return obstacleMap[
            row to column
        ]

    }

    /**
     * Returns an obstacle ONLY if this
     * cell is its top-left origin.
     */
    fun obstacleStartsAt(
        row: Int,
        column: Int
    ): Obstacle? {

        return obstacleStartMap[
            row to column
        ]

    }

    /**
     * Returns true if a seat or obstacle
     * occupies this position.
     */
    fun hasItem(
        row: Int,
        column: Int
    ): Boolean {

        return seatAt(row, column) != null
                ||
                obstacleAt(row, column) != null

    }

}