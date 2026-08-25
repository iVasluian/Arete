package com.cedacri.arete.data.repository

import com.cedacri.arete.data.remote.api.AuthApi
import com.cedacri.arete.domain.model.reservation.Reservation
import com.cedacri.arete.domain.model.reservation.ReservationStatus
import com.cedacri.arete.domain.repository.ReservationsRepository
import com.cedacri.arete.domain.repository.SeatReservationRepository
import java.time.LocalDate

class ReservationsRepositoryImpl(
    private val api: AuthApi
) : ReservationsRepository {

    override suspend fun getReservations(
        startDate: LocalDate,
        endDate: LocalDate,
        status: ReservationStatus
    ): Result<List<Reservation>> {

        return runCatching {

            val employees =
                api.getEmployees()

            val employeeById =
                employees.associateBy {
                    it.id.lowercase()
                }

            api.getReservationsRange(
                startDate = startDate.toString(),
                endDate = endDate.toString(),
                employeeId = "",
                status = status.value
            )
                .flatMap { office ->

                    office.reservations.map { reservation ->

                        val employee =
                            employeeById[
                                reservation.employeeId.lowercase()
                            ]

                        Reservation(
                            id = reservation.id,
                            employeeId =
                                reservation.employeeId,
                            firstName =
                                employee?.firstName.orEmpty(),
                            lastName =
                                employee?.lastName.orEmpty(),
                            group =
                                employee?.group?.name.orEmpty(),
                            seatName =
                                reservation.seatNameMemo,
                            startDate =
                                reservation.startDate,
                            endDate =
                                reservation.endDate,
                            createdBy =
                                reservation.createdBy,
                            deletedBy =
                                reservation.deletedBy
                        )
                    }
                }
        }
    }
}