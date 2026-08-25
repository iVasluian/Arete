package com.cedacri.arete.domain.repository

import com.cedacri.arete.domain.model.reservation.Reservation
import com.cedacri.arete.domain.model.reservation.ReservationStatus
import java.time.LocalDate

interface ReservationsRepository {

    suspend fun getReservations(
        startDate: LocalDate,
        endDate: LocalDate,
        status: ReservationStatus
    ): Result<List<Reservation>>
}