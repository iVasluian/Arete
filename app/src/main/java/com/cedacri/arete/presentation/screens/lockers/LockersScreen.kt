package com.cedacri.arete.presentation.screens.lockers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cedacri.arete.data.remote.response.OfficeResponse
import com.cedacri.arete.presentation.screens.lockers.components.LockerOfficeDropdown
import com.cedacri.arete.presentation.screens.lockers.components.LockerTable
import org.koin.androidx.compose.koinViewModel

@Composable
fun LockersScreen(
    viewModel: LockersViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LockersContent(
        uiState = uiState,
        onOfficeSelected = viewModel::selectOffice,
        onRefresh = viewModel::refresh
    )
}

@Composable
fun LockersContent(
    uiState: LockersUiState,
    onOfficeSelected: (Int?) -> Unit,
    onRefresh: () -> Unit
) {

    val selectedOffice =
        uiState.offices.firstOrNull {
            it.id == uiState.selectedOfficeId
        }

    val totalStats =
        remember(uiState.offices) {
            calculateTotalLockerStats(
                uiState.offices
            )
        }

    val selectedStats =
        selectedOffice?.let {
            calculateLockerStats(it)
        }

    val selectedOfficeLockers =
        remember(
            uiState.lockers,
            uiState.selectedOfficeId
        ) {

            if (uiState.selectedOfficeId == null) {

                uiState.lockers

            } else {

                uiState.lockers.filter {
                    it.officeId ==
                            uiState.selectedOfficeId
                }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.primary
            )
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        LockerOfficeDropdown(
            offices = uiState.offices,
            selectedOffice = selectedOffice,
            selectedStats = selectedStats,
            totalStats = totalStats,
            onOfficeSelected =
                onOfficeSelected
        )

        LockerTable(
            lockers = selectedOfficeLockers,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.height(89.dp))
    }
}

fun calculateLockerStats(
    office: OfficeResponse
): LockerStats {

    val reserved =
        office.lockers.count {
            !it.employeeId.isNullOrBlank()
        }

    val total =
        office.lockersCapacity

    val free =
        (total - reserved).coerceAtLeast(0)

    return LockerStats(
        total = total,
        free = free,
        reserved = reserved
    )
}

private fun calculateTotalLockerStats(
    offices: List<OfficeResponse>
): LockerStats {

    val total =
        offices.sumOf {
            it.lockersCapacity
        }

    val reserved =
        offices.sumOf { office ->

            office.lockers.count {
                !it.employeeId.isNullOrBlank()
            }
        }

    val free =
        (total - reserved).coerceAtLeast(0)

    return LockerStats(
        total = total,
        free = free,
        reserved = reserved
    )
}