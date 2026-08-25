package com.cedacri.arete.presentation.screens.seatReservation.grid

import com.cedacri.arete.domain.model.office.OfficeLayout

import com.cedacri.arete.domain.model.office.Seat

object OfficeGridBuilder {

    fun build(office: OfficeLayout): GridModel {
        val matrix = Matrix(
            initialRows = office.rows,
            columns = office.columns
        )

        placeObstacles(matrix, office)

        val seatsByLogicalRow = office.seats.groupBy { it.row }

        var visualRow = 1

        seatsByLogicalRow.toSortedMap().forEach { (_, seats) ->

            while (!canPlaceRow(matrix, visualRow, seats)) {
                visualRow++
            }

            placeRow(matrix, visualRow, seats)

            visualRow++
        }

        val rows = mutableListOf<GridRow>()

        var displayRow = 1

        for (row in 1..matrix.rows) {

            val cells = (1..matrix.columns).map { column ->
                GridCellModel(
                    row = row,
                    column = column,
                    content = matrix[row, column]
                )
            }

            val containsSeat = cells.any {
                it.content is GridContent.SeatContent
            }

            rows += GridRow(
                index = row,
                displayIndex = if (containsSeat) displayRow++ else null,
                cells = cells
            )
        }

        return GridModel(
            columns = office.columns,
            rows = rows
        )
    }

    private fun placeObstacles(matrix: Matrix, office: OfficeLayout) {
        office.obstacles.forEach { obstacle ->

            for (r in obstacle.row until obstacle.row + obstacle.rowSpan) {
                for (c in obstacle.column until obstacle.column + obstacle.columnSpan) {

                    if (r > matrix.rows || c > matrix.columns)
                        continue

                    if (r == obstacle.row && c == obstacle.column) {
                        matrix[r, c] = GridContent.ObstacleContent(obstacle)
                    } else {
                        matrix[r, c] = GridContent.Skip
                    }
                }
            }
        }
    }

    private fun canPlaceRow(
        matrix: Matrix,
        visualRow: Int,
        seats: List<Seat>
    ): Boolean {

        return seats.all { seat ->
            matrix[visualRow, seat.column] == GridContent.Empty
        }
    }

    private fun placeRow(
        matrix: Matrix,
        visualRow: Int,
        seats: List<Seat>
    ) {
        seats.forEach {

            matrix[visualRow, it.column] =
                GridContent.SeatContent(it)

        }
    }

    private class Matrix(
        initialRows: Int,
        val columns: Int
    ) {

        private val cells = mutableListOf<Array<GridContent>>()

        var rows = initialRows
            private set

        init {
            repeat(initialRows + 1) {
                addRow()
            }
        }

        operator fun get(row: Int, column: Int): GridContent {
            ensureRow(row)
            return cells[row][column]
        }

        operator fun set(
            row: Int,
            column: Int,
            value: GridContent
        ) {
            ensureRow(row)
            cells[row][column] = value
        }

        private fun ensureRow(row: Int) {
            while (row >= cells.size) {
                addRow()
            }

            rows = cells.size - 1
        }

        private fun addRow() {
            cells += Array(columns + 1) {
                GridContent.Empty
            }
        }
    }
}