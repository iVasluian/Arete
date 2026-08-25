package com.cedacri.arete.presentation.screens.lockers

import com.cedacri.arete.data.remote.response.OfficeResponse
import com.cedacri.arete.domain.model.office.Locker

data class LockersUiState(
    val lockers: List<Locker> = emptyList(),
    val offices: List<OfficeResponse> = emptyList(),
    val selectedOfficeId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
