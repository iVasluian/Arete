package com.cedacri.arete.data.repository

import com.cedacri.arete.data.mapper.LockerMapper
import com.cedacri.arete.data.mapper.OfficeGroupMapper
import com.cedacri.arete.data.mapper.OfficeLayoutMapper
import com.cedacri.arete.data.remote.api.AuthApi
import com.cedacri.arete.data.remote.response.EmployeeResponse
import com.cedacri.arete.data.remote.response.LockersData
import com.cedacri.arete.data.remote.response.OfficeResponse
import com.cedacri.arete.domain.model.office.OfficeGroup
import com.cedacri.arete.domain.model.office.OfficeLayout
import com.cedacri.arete.domain.model.reservation.ReservationRequest
import com.cedacri.arete.domain.repository.SeatReservationRepository
import java.time.LocalDate

class SeatReservationRepositoryImpl(
    private val api: AuthApi,
    private val mapper: OfficeLayoutMapper,
    private val groupMapper: OfficeGroupMapper,
    private val lockerMapper: LockerMapper
) : SeatReservationRepository {

    /**
     * Employees and offices almost never change.
     * We Cache them after the first request.
     */
    private var cachedEmployees: List<EmployeeResponse>? = null

    private var cachedOffices: List<OfficeResponse>? = null

    override suspend fun getOfficeLayouts(
        date: LocalDate
    ): Result<List<OfficeLayout>> {

        return runCatching {

            val employees =
                cachedEmployees ?: api.getEmployees().also {
                    cachedEmployees = it
                }

            val offices =
                cachedOffices ?: api.getOffices().also {
                    cachedOffices = it
                }

            val reservations =
                api.getReservations(date.toString())

            mapper.map(
                employees = employees,
                offices = offices,
                reservations = reservations
            )
        }
    }

    override suspend fun createReservation(
        seatId: Int,
        date: LocalDate
    ): Result<Int> {

        return runCatching {

            val response = api.createReservation(
                ReservationRequest(
                    seatId = seatId,
                    date = date.toString()
                )
            )

            if (response.isSuccessful) {
                response.body()?.id
                    ?: throw IllegalStateException(
                        "Reservation response contained no id"
                    )
            } else {
                throw IllegalStateException(
                    response.errorBody()
                        ?.string()
                        ?.takeIf { it.isNotBlank() }
                        ?: "Failed to create reservation"
                )
            }
        }
    }

    override suspend fun getGroups(): Result<List<OfficeGroup>> {
        return runCatching {
            val response = api.getGroups()

            groupMapper.map(response)
        }
    }

    override suspend fun getLockers(): Result<LockersData> {

        return runCatching {

            val employees =
                cachedEmployees ?: api.getEmployees().also {
                    cachedEmployees = it
                }

            val offices =
                cachedOffices ?: api.getOffices().also {
                    cachedOffices = it
                }

            LockersData(
                offices = offices,
                lockers = lockerMapper.map(
                    offices = offices,
                    employees = employees
                )
            )
        }
    }

    suspend fun clearCache() {
        cachedEmployees = null
        cachedOffices = null
    }
}