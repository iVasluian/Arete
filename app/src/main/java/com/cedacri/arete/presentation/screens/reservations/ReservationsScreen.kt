package com.cedacri.arete.presentation.screens.reservations

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cedacri.arete.domain.model.reservation.Reservation
import com.cedacri.arete.domain.model.reservation.ReservationStatus
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ReservationsScreen(
    viewModel: ReservationsViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    val snackbarHostState =
        remember {
            SnackbarHostState()
        }

    LaunchedEffect(uiState.error) {

        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->

        ReservationsContent(
            modifier =
                Modifier.padding(padding),
            uiState = uiState,
            onIntent =
                viewModel::onIntent
        )
    }
}

@Composable
fun ReservationsContent(
    modifier: Modifier = Modifier,
    uiState: ReservationsUiState,
    onIntent: (ReservationsIntent) -> Unit
) {

    val reservations =
        remember(
            uiState.reservations,
            uiState.searchQuery
        ) {
            filterReservations(
                reservations =
                    uiState.reservations,
                query =
                    uiState.searchQuery
            )
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.primary
            )
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Reservations",
            style =
                MaterialTheme.typography.headlineMedium,
            fontWeight =
                FontWeight.Bold,
            color =
                MaterialTheme.colorScheme.secondary
        )

        SearchField(
            value = uiState.searchQuery,
            onValueChange = {
                onIntent(
                    ReservationsIntent.SearchChanged(it)
                )
            }
        )

        DateFilters(
            startDate = uiState.startDate,
            endDate = uiState.endDate,
            onStartDateChanged = {
                onIntent(
                    ReservationsIntent.StartDateChanged(it)
                )
            },
            onEndDateChanged = {
                onIntent(
                    ReservationsIntent.EndDateChanged(it)
                )
            }
        )

        StatusDropdown(
            selectedStatus = uiState.status,
            onStatusSelected = {
                onIntent(
                    ReservationsIntent.StatusChanged(it)
                )
            }
        )

        ReservationsTable(
            reservations = reservations,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = {
            Text("Search")
        },
        placeholder = {
            Text(
                "Search reservations..."
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor =
                MaterialTheme.colorScheme.secondary,
            unfocusedTextColor =
                MaterialTheme.colorScheme.secondary,
            focusedLabelColor =
                MaterialTheme.colorScheme.secondary,
            unfocusedLabelColor =
                MaterialTheme.colorScheme.secondary,
            focusedBorderColor =
                MaterialTheme.colorScheme.secondary
        )
    )
}

@Composable
private fun DateFilters(
    startDate: LocalDate,
    endDate: LocalDate,
    onStartDateChanged: (LocalDate) -> Unit,
    onEndDateChanged: (LocalDate) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        DateFilterButton(
            label = "Start date",
            date = startDate,
            onDateSelected =
                onStartDateChanged,
            modifier =
                Modifier.weight(1f)
        )

        DateFilterButton(
            label = "End date",
            date = endDate,
            onDateSelected =
                onEndDateChanged,
            modifier =
                Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateFilterButton(
    label: String,
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {

    var showPicker by remember {
        mutableStateOf(false)
    }

    val formatter =
        remember {
            DateTimeFormatter.ofPattern(
                "yyyy MMM dd"
            )
        }

    Button(
        modifier = modifier,
        onClick = {
            showPicker = true
        },
        colors = ButtonDefaults.buttonColors(
            containerColor =
                MaterialTheme.colorScheme.secondary
        )
    ) {

        Text(
            text = "$label\n${date.format(formatter)}",
            color =
                MaterialTheme.colorScheme.primary
        )
    }

    if (showPicker) {

        val pickerState =
            rememberDatePickerState(
                initialSelectedDateMillis =
                    date.atStartOfDay(
                        ZoneId.systemDefault()
                    )
                        .toInstant()
                        .toEpochMilli()
            )

        DatePickerDialog(
            onDismissRequest = {
                showPicker = false
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        pickerState
                            .selectedDateMillis
                            ?.let { millis ->

                                val selectedDate =
                                    Instant
                                        .ofEpochMilli(
                                            millis
                                        )
                                        .atZone(
                                            ZoneId.systemDefault()
                                        )
                                        .toLocalDate()

                                onDateSelected(
                                    selectedDate
                                )
                            }

                        showPicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showPicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {

            DatePicker(
                state = pickerState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusDropdown(
    selectedStatus: ReservationStatus,
    onStatusSelected: (ReservationStatus) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selectedStatus.name
                .lowercase()
                .replaceFirstChar {
                    it.uppercase()
                },
            onValueChange = {},
            label = {
                Text("Status")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults
                    .TrailingIcon(
                        expanded = expanded
                    )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor =
                    MaterialTheme.colorScheme.secondary,
                unfocusedTextColor =
                    MaterialTheme.colorScheme.secondary,
                focusedLabelColor =
                    MaterialTheme.colorScheme.secondary,
                unfocusedLabelColor =
                    MaterialTheme.colorScheme.secondary,
                focusedBorderColor =
                    MaterialTheme.colorScheme.secondary
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            ReservationStatus.entries
                .forEach { status ->

                    DropdownMenuItem(
                        text = {
                            Text(
                                status.name
                                    .lowercase()
                                    .replaceFirstChar {
                                        it.uppercase()
                                    }
                            )
                        },
                        onClick = {

                            expanded = false

                            onStatusSelected(
                                status
                            )
                        }
                    )
                }
        }
    }
}

@Composable
private fun ReservationsTable(
    reservations: List<Reservation>,
    modifier: Modifier = Modifier
) {

    val horizontalScrollState =
        rememberScrollState()

    val verticalScrollState =
        rememberScrollState()

    Box(
        modifier = modifier
            .horizontalScroll(
                horizontalScrollState
            )
            .verticalScroll(
                verticalScrollState
            )
    ) {

        Column(
            modifier = Modifier.width(1100.dp)
        ) {

            ReservationTableHeader()

            reservations.forEach { reservation ->

                ReservationTableRow(
                    reservation = reservation
                )
            }
        }
    }
}

@Composable
private fun ReservationTableHeader() {

    ReservationTableRow(
        reservation = null
    )
}

@Composable
private fun ReservationTableRow(
    reservation: Reservation?
) {

    val header =
        reservation == null

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (header) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
            .padding(
                vertical = 12.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        ReservationCell(
            text =
                reservation?.employeeId
                    ?: "Matricola",
            width = 120.dp,
            header = header
        )

        ReservationCell(
            text =
                reservation?.firstName
                    ?: "First name",
            width = 130.dp,
            header = header
        )

        ReservationCell(
            text =
                reservation?.lastName
                    ?: "Last name",
            width = 130.dp,
            header = header
        )

        ReservationCell(
            text =
                reservation?.group
                    ?: "Group",
            width = 120.dp,
            header = header
        )

        ReservationCell(
            text =
                reservation?.seatName
                    ?: "Seat",
            width = 120.dp,
            header = header
        )

        ReservationCell(
            text =
                reservation
                    ?.startDate
                    ?.formatReservationDate()
                    ?: "Start date",
            width = 130.dp,
            header = header
        )

        ReservationCell(
            text =
                reservation
                    ?.endDate
                    ?.formatReservationDate()
                    ?: "End date",
            width = 130.dp,
            header = header
        )

        ReservationCell(
            text =
                reservation?.createdBy
                    ?: "Created by",
            width = 120.dp,
            header = header
        )

        ReservationCell(
            text =
                reservation?.deletedBy
                    ?.takeIf { it.isNotBlank() }
                    ?: if (header) {
                        "Deleted by"
                    } else {
                        ""
                    },
            width = 120.dp,
            header = header
        )
    }
}

@Composable
private fun ReservationCell(
    text: String,
    width: androidx.compose.ui.unit.Dp,
    header: Boolean
) {

    Text(
        text = text,
        modifier = Modifier
            .width(width)
            .padding(
                horizontal = 8.dp
            ),
        color =
            if (header) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondary
            },
        fontWeight =
            if (header) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
        style =
            MaterialTheme.typography.bodySmall
    )
}

private fun String.formatReservationDate(): String {

    return runCatching {

        java.time.LocalDateTime
            .parse(this)
            .toLocalDate()
            .format(
                DateTimeFormatter.ofPattern(
                    "yyyy MMM dd"
                )
            )

    }.getOrDefault(this)
}

private fun filterReservations(
    reservations: List<Reservation>,
    query: String
): List<Reservation> {

    if (query.isBlank()) {
        return reservations
    }

    val search =
        query.trim().lowercase()

    return reservations.filter { reservation ->

        reservation.employeeId
            .lowercase()
            .contains(search) ||

                reservation.firstName
                    .lowercase()
                    .contains(search) ||

                reservation.lastName
                    .lowercase()
                    .contains(search) ||

                reservation.group
                    .lowercase()
                    .contains(search) ||

                reservation.seatName
                    .lowercase()
                    .contains(search) ||

                reservation.startDate
                    .lowercase()
                    .contains(search) ||

                reservation.endDate
                    .lowercase()
                    .contains(search) ||

                reservation.createdBy
                    .lowercase()
                    .contains(search) ||

                reservation.deletedBy
                    .lowercase()
                    .contains(search)
    }
}