package com.cedacri.arete.presentation.screens.preferences.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.cedacri.arete.domain.model.preferences.Appearance

@Composable
fun AppearanceSection(
    appearance: Appearance,
    onAppearanceChanged: (Appearance) -> Unit
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Appearance",
            style =
                MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        AppearanceOption(
            title = "System Default",
            selected =
                appearance == Appearance.SYSTEM,
            onClick = {
                onAppearanceChanged(
                    Appearance.SYSTEM
                )
            }
        )

        AppearanceOption(
            title = "Light",
            selected =
                appearance == Appearance.LIGHT,
            onClick = {
                onAppearanceChanged(
                    Appearance.LIGHT
                )
            }
        )

        AppearanceOption(
            title = "Dark",
            selected =
                appearance == Appearance.DARK,
            onClick = {
                onAppearanceChanged(
                    Appearance.DARK
                )
            }
        )
    }
}