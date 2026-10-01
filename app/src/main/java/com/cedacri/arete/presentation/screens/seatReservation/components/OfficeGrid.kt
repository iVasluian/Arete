package com.cedacri.arete.presentation.screens.seatReservation.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.cedacri.arete.domain.model.office.OfficeGroup
import com.cedacri.arete.domain.model.office.OfficeLayout
import com.cedacri.arete.domain.model.office.Seat
import com.cedacri.arete.presentation.screens.seatReservation.grid.GridContent
import com.cedacri.arete.presentation.screens.seatReservation.grid.OfficeGridBuilder

private val CellSize = 64.dp

@Composable
fun OfficeGrid(
    office: OfficeLayout,
    groups: List<OfficeGroup>,
    selectedGroupIds: Set<Int>,
    currentUserGroup: OfficeGroup?,
    availableSeatColor: String,
    zoom: Float,
    modifier: Modifier = Modifier,
    onSeatClick: (Seat) -> Unit
) {
    val cellSize by remember (zoom) { mutableStateOf(CellSize * zoom) }

    val grid = remember(office) {
        OfficeGridBuilder.build(office)
    }

    val selectedGroups = remember(
        groups,
        selectedGroupIds
    ) {
        groups.filter {
            it.id in selectedGroupIds
        }
    }

    val groupByEmployeeId = remember(
        selectedGroups
    ) {

        buildMap {
            selectedGroups.forEach { group ->
                group.employeeIds.forEach { employeeId ->
                    put(
                        employeeId.lowercase(),
                        group
                    )
                }
            }
        }
    }

    val obstaclesByCell = remember(grid) {

        buildMap {
            grid.rows.forEach { row ->
                row.cells.forEach { cell ->
                    val obstacle =
                        (cell.content as?
                                GridContent.ObstacleContent)
                            ?.obstacle
                            ?: return@forEach
                    put(
                        cell.row to cell.column,
                        obstacle
                    )
                }
            }
        }
    }

    Layout(
        modifier = modifier,
        content = {
            /**
             * Top-left empty corner
             */
            Box(
                Modifier
                    .layoutId("corner")
                    .size(cellSize)
            )

            /**
             * Column headers
             */
            Log.d("OfficeGrid", "Columns = ${grid.columnsWithObstacles}")
            Log.d(
                "OfficeGrid",
                "Max seat column = ${office.seats.maxOfOrNull { it.column }}"
            )
            repeat(grid.columns) { column ->

                HeaderCell(
                    modifier = Modifier.layoutId("column_$column"),
                    text = ('A' + column).toString()
                )
            }

            /**
             * Row headers
             */
            grid.rows.forEach { row ->
                HeaderCell(
                    modifier = Modifier.layoutId("row_${row.index}"),
                    text = row.displayIndex?.toString().orEmpty()
                )
            }

            /**
             * Office cells
             */
            grid.rows.forEach { row ->
                row.cells.forEach { cell ->
                    when (val content = cell.content) {
                        GridContent.Empty, GridContent.Skip -> {
//                            EmptyCell(
//                                modifier = Modifier.layoutId(
//                                    "cell_${cell.row}_${cell.column}"
//                                )
//
//                            )

                        }

                        is GridContent.SeatContent -> {
                            val canReserve =
                                content.seat.active &&
                                        content.seat.reservedBy == null &&
                                        currentUserGroup
                                            ?.seatIds
                                            ?.contains(content.seat.id) == true

                            if (content.seat.active) {

                                SeatCell(
                                    modifier = Modifier.layoutId(
                                        "cell_${cell.row}_${cell.column}"
                                    ),
                                    seat = content.seat,
                                    group = content.seat.reservedBy
                                        ?.id
                                        ?.lowercase()
                                        ?.let(groupByEmployeeId::get),
                                    canReserve = canReserve,
                                    availableSeatColor = parseGroupColor(
                                        availableSeatColor
                                    ),
                                    onClick = {
                                        onSeatClick(content.seat)
                                    }
                                )
                            }
                        }

                        is GridContent.ObstacleContent -> {
                            ObstacleCell(
                                modifier = Modifier.layoutId(
                                    "cell_${cell.row}_${cell.column}"
                                ),
                                obstacle = content.obstacle
                            )
                        }
                    }

                }

            }

        }

    ) { measurables, constraints ->
        val cellPx = cellSize.roundToPx()

        val officeWidth =
            grid.columnsWithObstacles * cellPx

        val officeHeight =
            grid.rows.size * cellPx

        val totalWidth =
            officeWidth + cellPx

        val totalHeight =
            officeHeight + cellPx

        val placeables = mutableMapOf<String, androidx.compose.ui.layout.Placeable>()

        measurables.forEach { measurable ->

            val id = measurable.layoutId.toString()

            val placeable = when {

                id == "corner" -> {

                    measurable.measure(
                        Constraints.fixed(
                            cellPx,
                            cellPx
                        )
                    )

                }

                id.startsWith("column_") -> {

                    measurable.measure(
                        Constraints.fixed(
                            cellPx,
                            cellPx
                        )
                    )

                }

                id.startsWith("row_") -> {

                    measurable.measure(
                        Constraints.fixed(
                            cellPx,
                            cellPx
                        )
                    )

                }

                id.startsWith("cell_") -> {

                    val parts =
                        id.removePrefix("cell_")
                            .split("_")

                    val row =
                        parts[0].toInt()

                    val column =
                        parts[1].toInt()

                    val obstacle =
                        obstaclesByCell[
                            row to column
                        ]

                    val width =
                        (obstacle?.columnSpan ?: 1) * cellPx

                    val height =
                        (obstacle?.rowSpan ?: 1) * cellPx

                    measurable.measure(
                        Constraints.fixed(
                            width,
                            height
                        )
                    )

                }

                else -> {

                    measurable.measure(
                        Constraints.fixed(
                            cellPx,
                            cellPx
                        )
                    )

                }

            }

            placeables[id] = placeable

        }

        layout(
            width = totalWidth,
            height = totalHeight
        ) {

            /**
             * Empty top-left corner
             */
            placeables["corner"]?.placeRelative(
                0,
                0
            )

            /**
             * Column headers
             */
            repeat(grid.columns) { column ->

                placeables["column_$column"]
                    ?.placeRelative(

                        x = cellPx + column * cellPx,

                        y = 0

                    )

            }

            /**
             * Row headers
             */
            grid.rows.forEach { row ->

                placeables["row_${row.index}"]
                    ?.placeRelative(

                        x = 0,

                        y = cellPx + (row.index - 1) * cellPx

                    )

            }

            /**
             * Office cells
             */
            grid.rows.forEach { row ->

                row.cells.forEach { cell ->

                    if (cell.content == GridContent.Skip)
                        return@forEach

                    placeables[
                        "cell_${cell.row}_${cell.column}"
                    ]?.placeRelative(

                        x = cellPx + (cell.column - 1) * cellPx,

                        y = cellPx + (cell.row - 1) * cellPx

                    )

                }

            }

        }

    }

}

@Composable
private fun HeaderCell(
    modifier: Modifier = Modifier,
    text: String
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )

    }

}