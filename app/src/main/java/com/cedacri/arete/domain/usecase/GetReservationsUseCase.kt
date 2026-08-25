package com.cedacri.arete.domain.usecase

import com.cedacri.arete.domain.model.reservation.Reservation
import com.cedacri.arete.domain.model.reservation.ReservationStatus
import com.cedacri.arete.domain.repository.ReservationsRepository
import java.time.LocalDate

class GetReservationsUseCase(
    private val repository: ReservationsRepository
) {

    suspend operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
        status: ReservationStatus
    ): Result<List<Reservation>> {

        return repository.getReservations(
            startDate = startDate,
            endDate = endDate,
            status = status
        )
    }
}