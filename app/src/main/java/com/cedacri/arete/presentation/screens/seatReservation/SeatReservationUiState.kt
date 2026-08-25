package com.cedacri.arete.presentation.screens.seatReservation

import com.cedacri.arete.domain.model.office.Employee
import com.cedacri.arete.domain.model.office.OfficeGroup
import com.cedacri.arete.domain.model.office.OfficeLayout
import com.cedacri.arete.domain.model.office.Seat
import java.time.LocalDate
data class SeatReservationUiState(
    val isLoading: Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val offices: List<OfficeLayout> = emptyList(),
    val selectedOfficeId: Int? = null,
    val selectedSeat: Seat? = null,
    val selectedEmployee: Employee? = null,
    val groups: List<OfficeGroup> = emptyList(),
    val selectedGroupIds: Set<Int> = emptySet(),
    val currentUserGroup: OfficeGroup? = null,
    val error: String? = null
) {

    val selectedOffice: OfficeLayout?
        get() = offices.firstOrNull {
            it.officeId == selectedOfficeId
        }

}