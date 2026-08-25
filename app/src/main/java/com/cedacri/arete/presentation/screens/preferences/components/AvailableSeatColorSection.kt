package com.cedacri.arete.presentation.screens.preferences.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cedacri.arete.presentation.screens.seatReservation.components.parseGroupColor

@Composable
fun AvailableSeatColorSection(
    color: String,
    onColorChanged: (String) -> Unit
) {

    var showPicker by remember {
        mutableStateOf(false)
    }

    val currentColor = parseGroupColor(color)

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Available seat color",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .clickable {
                    showPicker = true
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .background(currentColor)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(10.dp)
                    )
            )

            Column {

                Text(
                    text = "Available seat",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = color.uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showPicker) {

        ColorPickerDialog(
            initialColor = currentColor,
            onColorSelected = {
                onColorChanged(it.toHex())
            },
            onDismiss = {
                showPicker = false
            }
        )
    }
}

private fun isValidHexColor(
    value: String
): Boolean {

    return Regex(
        "^#[0-9A-Fa-f]{6}$"
    ).matches(value)
}