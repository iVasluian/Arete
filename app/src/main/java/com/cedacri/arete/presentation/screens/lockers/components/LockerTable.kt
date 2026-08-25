package com.cedacri.arete.presentation.screens.lockers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cedacri.arete.domain.model.office.Locker

@Composable
fun LockerTable(
    lockers: List<Locker>,
    modifier: Modifier = Modifier
) {

    val horizontalScrollState =
        rememberScrollState()

    val verticalScrollState =
        rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.outline)
            .verticalScroll(
                verticalScrollState
            )
    ) {

        Row(
            modifier = Modifier
                .horizontalScroll(
                    horizontalScrollState
                )
        ) {

            Column {

                LockerTableHeader()

                HorizontalDivider(
                    Modifier.background(MaterialTheme.colorScheme.outline)
                )

                lockers.forEachIndexed { index, locker ->

                    val backgroundColor =
                        if (index % 2 == 0) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.2f
                            )
                        }

                    LockerTableRow(
                        color = backgroundColor,
                        locker = locker
                    )
                }
            }
        }
    }
}