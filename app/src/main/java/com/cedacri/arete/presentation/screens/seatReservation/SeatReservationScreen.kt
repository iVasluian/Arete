package com.cedacri.arete.presentation.screens.seatReservation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cedacri.arete.data.local.datasource.TokenStorage
import com.cedacri.arete.domain.model.office.Employee
import com.cedacri.arete.domain.model.office.OfficeGroup
import com.cedacri.arete.domain.model.office.OfficeLayout
import com.cedacri.arete.domain.model.office.Seat
import com.cedacri.arete.presentation.screens.seatReservation.components.OfficeGrid
import com.cedacri.arete.presentation.screens.seatReservation.components.parseGroupColor
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun OfficeMapScreen(
    viewModel: SeatReservationViewModel = koinViewModel(),
    tokenStorage: TokenStorage = koinInject()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val availableSeatColor by tokenStorage
        .getAvailableSeatColorFlow()
        .collectAsState(
            initial = "#43A047"
        )

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SeatReservationEvent.ShowError -> {
                    snackBarHostState.showSnackbar(
                        event.message
                    )
                }

                is SeatReservationEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(
                        event.message
                    )

                }

            }

        }

    }

    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier.padding(top = 16.dp))
            }
        }
    ) { padding ->
        SeatReservationContent(
            modifier = Modifier.padding(padding),
            uiState = uiState,
            availableSeatColor = availableSeatColor,
            onIntent = viewModel::onIntent
        )

    }

}

@Composable
private fun SeatReservationContent(
    modifier: Modifier = Modifier,
    uiState: SeatReservationUiState,
    availableSeatColor: String,
    onIntent: (SeatReservationIntent) -> Unit
) {
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    var scale by remember {
        mutableFloatStateOf(1f)
    }

    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)

    }

    val visibleGroups = remember(
        uiState.groups,
        uiState.selectedOffice
    ) {

        uiState.selectedOffice?.let { office ->

            getGroupsForOffice(
                groups = uiState.groups,
                office = office
            )

        } ?: emptyList()
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        DateSection(
            selectedDate = uiState.selectedDate,
            onDateSelected = {
                onIntent(
                    SeatReservationIntent.DateChanged(it)
                )
            }
        )

        OfficeDropdown(
            offices = uiState.offices,
            selectedOfficeId = uiState.selectedOfficeId,
            onOfficeSelected = {
                onIntent(
                    SeatReservationIntent.OfficeChanged(it)
                )
            }
        )

        GroupFilter(
            groups = visibleGroups,
            selectedGroupIds = uiState.selectedGroupIds,
            onGroupToggle = {
                onIntent(
                    SeatReservationIntent.GroupToggled(it)
                )
            }
        )

        uiState.selectedOffice?.let { office ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .graphicsLayer{
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
                    .horizontalScroll(horizontalScrollState)
                    .verticalScroll(verticalScrollState)
//                    .transformable(state)
            ) {

                OfficeGrid(
                    office = office,
                    groups = visibleGroups,
                    selectedGroupIds = uiState.selectedGroupIds,
                    currentUserGroup = uiState.currentUserGroup,
                    availableSeatColor = availableSeatColor,
                    onSeatClick = { seat ->
                        onIntent(SeatReservationIntent.SeatClicked(seat))
                    }
                )
            }

            Spacer(
                Modifier.height(78.dp)
            )
        }

    }

    uiState.selectedSeat?.let { seat ->

        ReservationConfirmationDialog(
            seat = seat,
            selectedDate = uiState.selectedDate,
            onConfirm = {
                onIntent(
                    SeatReservationIntent.ConfirmReservation
                )
            },
            onDismiss = {
                onIntent(
                    SeatReservationIntent.CancelReservation
                )
            }
        )
    }

    uiState.selectedEmployee?.let { employee ->

        EmployeeInfoDialog(
            employee = employee,
            onDismiss = {
                onIntent(
                    SeatReservationIntent.DismissEmployeeInfo
                )
            }
        )
    }

}

@Composable
private fun DateSection(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()

    val nextWorkingDay = remember(today) {
        getNextWorkingDay(today)
    }

    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("dd MMM yyyy")
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        DateButton(
            date = today,
            selected = selectedDate == today,
            formatter = dateFormatter,
            onClick = {
                onDateSelected(today)
            },
            modifier = Modifier.weight(1f)
        )

        DateButton(
            date = nextWorkingDay,
            selected = selectedDate == nextWorkingDay,
            formatter = dateFormatter,
            onClick = {
                onDateSelected(nextWorkingDay)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DateButton(
    date: LocalDate,
    selected: Boolean,
    formatter: DateTimeFormatter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (selected) {

        Button(
            modifier = modifier,
            onClick = onClick,
            colors =  ButtonDefaults.buttonColors().copy(
                contentColor = MaterialTheme.colorScheme.secondary,
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text(
                text = date.format(formatter)
            )
        }

    } else {

        OutlinedButton(
            modifier = modifier,
            onClick = onClick
        ) {
            Text(
                text = date.format(formatter)
            )
        }

    }
}

private fun getNextWorkingDay(
    date: LocalDate
): LocalDate {

    var nextDate = date.plusDays(1)

    while (
        nextDate.dayOfWeek == DayOfWeek.SATURDAY ||
        nextDate.dayOfWeek == DayOfWeek.SUNDAY
    ) {
        nextDate = nextDate.plusDays(1)
    }

    return nextDate
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OfficeDropdown(
    offices: List<OfficeLayout>,
    selectedOfficeId: Int?,
    onOfficeSelected: (Int) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val selected = offices.firstOrNull {
        it.officeId == selectedOfficeId
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        OutlinedTextField(
            modifier = Modifier.menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selected?.officeTitle ?: "",
            onValueChange = {},
            label = {
                Text("Office",
                    color = MaterialTheme.colorScheme.secondary)
            },

            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                focusedBorderColor = MaterialTheme.colorScheme.secondary
            )


        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            offices.forEach { office ->
                DropdownMenuItem(
                    text = {
                        Text(office.officeTitle,
                            textAlign = TextAlign.Center)
                    },
                    onClick = {
                        expanded = false
                        onOfficeSelected(
                            office.officeId
                        )
                    }
                )
            }
        }
    }

}

@Composable
private fun ReservationConfirmationDialog(
    seat: Seat,
    selectedDate: LocalDate,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.border((1.5f).dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(16.dp)),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Reserve seat?",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Do you want to reserve this seat?",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = seat.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = selectedDate.format(
                        DateTimeFormatter.ofPattern(
                            "dd MMM yyyy"
                        )
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text("Reserve",
                    color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel",
                    color = MaterialTheme.colorScheme.secondary)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupFilter(
    groups: List<OfficeGroup>,
    selectedGroupIds: Set<Int>,
    onGroupToggle: (Int) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val selectedGroups = groups.filter {
        it.id in selectedGroupIds
    }

    val fieldText = when {

        selectedGroups.isEmpty() ->
            "All groups"

        selectedGroups.size == 1 ->
            selectedGroups.first().name

        else ->
            "${selectedGroups.size} groups selected"
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
            value = fieldText,
            onValueChange = {},
            label = {
                Text(
                    text = "Groups",
                    color = MaterialTheme.colorScheme.secondary
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

            groups.forEach { group ->

                val selected =
                    group.id in selectedGroupIds

                DropdownMenuItem(
                    text = {

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically,
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(
                                        RoundedCornerShape(4.dp)
                                    )
                                    .background(
                                        parseGroupColor(
                                            group.color
                                        )
                                    )
                            )

                            Text(
                                text = group.name
                            )
                        }
                    },
                    trailingIcon = {
                        Checkbox(
                            checked = selected,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.secondary,
                                uncheckedColor = MaterialTheme.colorScheme.secondary,
                                checkmarkColor = Color.Transparent
                            )
                        )
                    },
                    onClick = {
                        onGroupToggle(group.id)
                    }
                )
            }
        }
    }
}

fun getOfficeEmployeeIds(
    office: OfficeLayout
): Set<String> {

    return office.seats
        .mapNotNull {
            it.reservedBy?.id
        }
        .map {
            it.lowercase()
        }
        .toSet()
}

private fun getGroupsForOffice(
    groups: List<OfficeGroup>,
    office: OfficeLayout
): List<OfficeGroup> {

    val officeEmployeeIds =
        office.seats
            .mapNotNull {
                it.reservedBy?.id
            }
            .map {
                it.lowercase()
            }
            .toSet()

    val officeSeatIds =
        office.seats
            .map {
                it.id
            }
            .toSet()

    return groups.filter { group ->

        val hasEmployeeInOffice =
            group.employeeIds.any {
                it in officeEmployeeIds
            }

        val hasSeatsInOffice =
            group.seatIds.any {
                it in officeSeatIds
            }

        hasEmployeeInOffice &&
                hasSeatsInOffice
    }
}

@Composable
private fun EmployeeInfoDialog(
    employee: Employee,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border((1.5f).dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Reserved seat",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = employee.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = employee.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = employee.group,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = employee.id,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                TextButton(
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    ),
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Close",
                        color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}