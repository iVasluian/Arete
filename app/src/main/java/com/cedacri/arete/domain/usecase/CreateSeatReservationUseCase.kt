package com.cedacri.arete.domain.usecase
import com.cedacri.arete.domain.repository.SeatReservationRepository
import java.time.LocalDate

class CreateSeatReservationUseCase(
    private val repository: SeatReservationRepository
) {

    suspend operator fun invoke(
        seatId: Int,
        date: LocalDate
    ): Result<Int> {

        return repository.createReservation(
            seatId = seatId,
            date = date
        )
    }
}