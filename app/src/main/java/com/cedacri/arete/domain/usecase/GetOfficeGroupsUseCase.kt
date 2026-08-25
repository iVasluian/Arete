package com.cedacri.arete.domain.usecase

import com.cedacri.arete.domain.model.office.OfficeGroup
import com.cedacri.arete.domain.repository.SeatReservationRepository

class GetOfficeGroupsUseCase(
    private val repository: SeatReservationRepository
) {

    suspend operator fun invoke(): Result<List<OfficeGroup>> {
        return repository.getGroups()
    }
}