package com.cedacri.arete.domain.repository

import com.cedacri.arete.data.remote.response.LockersData
import com.cedacri.arete.domain.model.office.Locker
import com.cedacri.arete.domain.model.office.OfficeGroup
import com.cedacri.arete.domain.model.office.OfficeLayout
import java.time.LocalDate

interface SeatReservationRepository {

    suspend fun getOfficeLayouts(
        date: LocalDate
    ): Result<List<OfficeLayout>>

    suspend fun createReservation(
        seatId: Int,
        date: LocalDate
    ): Result<Int>

    suspend fun getGroups(): Result<List<OfficeGroup>>

    suspend fun getLockers(): Result<LockersData>
}