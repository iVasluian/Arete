package com.cedacri.arete.presentation.screens.officeManagement

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cedacri.arete.presentation.screens.lockers.LockersContent
import com.cedacri.arete.presentation.screens.lockers.LockersViewModel
import com.cedacri.arete.presentation.screens.reservations.ReservationsContent
import com.cedacri.arete.presentation.screens.reservations.ReservationsViewModel
import org.koin.androidx.compose.koinViewModel

private enum class OfficeManagementSection {
    LOCKERS,
    RESERVATIONS
}

@Composable
fun OfficeManagementScreen() {

    val lockersViewModel:
            LockersViewModel = koinViewModel()

    val reservationsViewModel:
            ReservationsViewModel = koinViewModel()

    val lockersState by
    lockersViewModel.uiState
        .collectAsStateWithLifecycle()

    val reservationsState by
    reservationsViewModel.uiState
        .collectAsStateWithLifecycle()

    var selectedSection by remember {
        mutableStateOf(
            OfficeManagementSection.LOCKERS
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.primary
            )
    ) {

        OfficeManagementSelector(
            selectedSection = selectedSection,
            onSectionSelected = {
                selectedSection = it
            }
        )

        when (selectedSection) {

            OfficeManagementSection.LOCKERS -> {

                LockersContent(
                    uiState = lockersState,
                    onOfficeSelected = {
                        lockersViewModel
                            .selectOffice(it)
                    },
                    onRefresh = {
                        lockersViewModel
                            .refresh()
                    }
                )
            }

            OfficeManagementSection.RESERVATIONS -> {

                ReservationsContent(
                    uiState = reservationsState,
                    onIntent =
                        reservationsViewModel::onIntent
                )
            }
        }
    }
}

@Composable
private fun OfficeManagementSelector(
    selectedSection: OfficeManagementSection,
    onSectionSelected:
        (OfficeManagementSection) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        SectionButton(
            text = "Lockers",
            selected =
                selectedSection ==
                        OfficeManagementSection.LOCKERS,
            onClick = {
                onSectionSelected(
                    OfficeManagementSection.LOCKERS
                )
            },
            modifier = Modifier.weight(1f)
        )

        SectionButton(
            text = "Reservations",
            selected =
                selectedSection ==
                        OfficeManagementSection.RESERVATIONS,
            onClick = {
                onSectionSelected(
                    OfficeManagementSection.RESERVATIONS
                )
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SectionButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (selected) {

        Button(
            modifier = modifier,
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor =
                    MaterialTheme.colorScheme.tertiary,
                contentColor =
                    MaterialTheme.colorScheme.secondary
            )
        ) {

            Text(text)
        }

    } else {

        OutlinedButton(
            modifier = modifier,
            onClick = onClick
        ) {

            Text(text)
        }
    }
}