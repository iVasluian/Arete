package com.cedacri.arete.presentation.screens.preferences

import com.cedacri.arete.domain.model.preferences.Appearance

data class PreferencesUiState(
    val appearance: Appearance = Appearance.SYSTEM,
    val availableSeatColor: String = "#1A000000"
)