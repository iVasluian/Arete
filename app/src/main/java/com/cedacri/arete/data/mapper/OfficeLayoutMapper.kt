package com.cedacri.arete.data.mapper

import com.cedacri.arete.domain.model.office.Employee
import com.cedacri.arete.data.remote.response.EmployeeResponse
import com.cedacri.arete.data.remote.response.MetadataResponse
import com.cedacri.arete.domain.model.office.Obstacle
import com.cedacri.arete.data.remote.response.ObstacleResponse
import com.cedacri.arete.domain.model.office.ObstacleType
import com.cedacri.arete.domain.model.office.OfficeLayout
import com.cedacri.arete.data.remote.response.OfficeReservationsResponse
import com.cedacri.arete.data.remote.response.OfficeResponse
import com.cedacri.arete.data.remote.response.ReservationResponse
import com.cedacri.arete.domain.model.office.Seat
import com.cedacri.arete.data.remote.response.SeatResponse
import com.cedacri.arete.presentation.screens.seatReservation.SeatCoordinateParser
import com.google.gson.Gson
import kotlin.collections.flatMap
import kotlin.collections.map

class OfficeLayoutMapper(
    private val gson: Gson
) {

    private class VisualMatrix(
        rows: Int,
        columns: Int
    ) {

        private val occupied =
            Array(rows + 1) {
                BooleanArray(columns + 1)
            }

        fun occupy(
            row: Int,
            column: Int,
            rowSpan: Int,
            columnSpan: Int
        ) {

            for (r in row until row + rowSpan) {

                for (c in column until column + columnSpan) {

                    occupied[r][c] = true

                }

            }

        }

        fun isOccupied(
            row: Int,
            column: Int
        ): Boolean {

            return occupied[row][column]

        }

    }

    fun map(
        employees: List<EmployeeResponse>,
        offices: List<OfficeResponse>,
        reservations: List<OfficeReservationsResponse>
    ): List<OfficeLayout> {

        val employeesById = buildEmployeeLookup(employees)

        val reservationsBySeatId =
            buildReservationLookup(reservations)

        return offices.map { office ->

            mapOffice(
                office,
                employeesById,
                reservationsBySeatId
            )

        }
    }

    private fun buildEmployeeLookup(
        employees: List<EmployeeResponse>
    ): Map<String, Employee> {

        return employees.associate {

            it.id.lowercase() to Employee(
                id = it.id,
                firstName = it.firstName.orEmpty(),
                lastName = it.lastName.orEmpty(),
                email = it.email.orEmpty(),
                group = it.group?.name?.uppercase().orEmpty()
            )

        }
    }

    private fun buildReservationLookup(
        reservations: List<OfficeReservationsResponse>
    ): Map<Int, ReservationResponse> {

        return reservations
            .flatMap { it.reservations }
            .associateBy {
                it.seatId
            }
    }

    private fun mapOffice(
        office: OfficeResponse,
        employeesById: Map<String, Employee>,
        reservationsBySeatId: Map<Int, ReservationResponse>
    ): OfficeLayout {

        val metadata =
            parseMetadata(office.metadata)

        val seats =
            office.seats.mapNotNull {

                mapSeat(
                    seat = it,
                    employeesById = employeesById,
                    reservationsBySeatId = reservationsBySeatId
                )

            }

        val obstacles =
            metadata.obstacles.map(::mapObstacle)

        return OfficeLayout(
            officeId = office.id,
            officeName = office.name,
            columns = office.columns,
            seats = seats,
            obstacles = obstacles
        )
    }

    private fun mapSeat(
        seat: SeatResponse,
        employeesById: Map<String, Employee>,
        reservationsBySeatId: Map<Int, ReservationResponse>
    ): Seat? {

        val employee =
            reservationsBySeatId[seat.id]
                ?.employeeId
                ?.lowercase()
                ?.let(employeesById::get)

        val (row,column)=
            SeatCoordinateParser.parse(
                seat.name
            )

        return Seat(
            id = seat.id,
            name = seat.name,
            row = row,
            column = column,
            active = seat.active,
            reservedBy = employee
        )
    }

    private fun mapObstacle(
        obstacle: ObstacleResponse
    ): Obstacle {

        return Obstacle(
            id = obstacle.id,
            type = obstacle.type.toObstacleType(),
            row = obstacle.row,
            column = obstacle.col,
            rowSpan = obstacle.rowSpan,
            columnSpan = obstacle.colSpan,
            label = obstacle.label
        )
    }

    private fun parseMetadata(
        metadata: String?
    ): MetadataResponse {

        if (metadata.isNullOrBlank()) {
            return MetadataResponse(
                obstacles = emptyList()
            )
        }

        return runCatching {

            gson.fromJson(
                metadata,
                MetadataResponse::class.java
            )

        }.getOrNull()
            ?: MetadataResponse(
                obstacles = emptyList()
            )
    }

    private fun String.toObstacleType(): ObstacleType =
        when (lowercase()) {
            "door" -> ObstacleType.DOOR
            "window" -> ObstacleType.WINDOW
            "locker" -> ObstacleType.LOCKER
            "wardrobe" -> ObstacleType.WARDROBE
            else -> ObstacleType.UNKNOWN
        }
}