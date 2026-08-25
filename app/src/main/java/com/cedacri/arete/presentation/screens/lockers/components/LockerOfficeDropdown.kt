package com.cedacri.arete.presentation.screens.lockers.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.cedacri.arete.data.remote.response.OfficeResponse
import com.cedacri.arete.presentation.screens.lockers.LockerStats
import com.cedacri.arete.presentation.screens.lockers.calculateLockerStats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockerOfficeDropdown(
    offices: List<OfficeResponse>,
    selectedOffice: OfficeResponse?,
    selectedStats: LockerStats?,
    totalStats: LockerStats,
    onOfficeSelected: (Int?) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val selectedText =
        selectedOffice?.let { office ->

            val stats =
                selectedStats
                    ?: LockerStats(
                        total = 0,
                        free = 0,
                        reserved = 0
                    )

            "${office.name} " +
                    "${stats.total} " +
                    "(${stats.free} free, " +
                    "${stats.reserved} reserved)"

        } ?: run {

            "All offices " +
                    "${totalStats.total} " +
                    "(${totalStats.free} free, " +
                    "${totalStats.reserved} reserved)"
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
            value = selectedText,
            onValueChange = {},
            label = {
                Text(
                    text = "Office",
                    color =
                        MaterialTheme.colorScheme.secondary
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

            DropdownMenuItem(
                text = {

                    Text(
                        "All offices " +
                                "${totalStats.total} " +
                                "(${totalStats.free} free, " +
                                "${totalStats.reserved} reserved)"
                    )
                },
                onClick = {

                    expanded = false

                    onOfficeSelected(null)
                }
            )

            offices
                .filter { it.lockersCapacity > 0 }
                .forEach { office ->

                    val stats =
                        calculateLockerStats(office)

                    DropdownMenuItem(
                        text = {
                            Text(
                                "${office.name} " +
                                        "${stats.total} " +
                                        "(${stats.free} free, " +
                                        "${stats.reserved} reserved)"
                            )
                        },
                        onClick = {
                            expanded = false
                            onOfficeSelected(office.id)
                        }
                    )
                }
        }
    }
}