package com.cedacri.arete.common

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.cedacri.arete.presentation.icons.bookmark_check
import com.cedacri.arete.presentation.icons.map
import com.cedacri.arete.presentation.icons.settings
import com.cedacri.arete.presentation.navigation.OfficeMap
import com.cedacri.arete.presentation.navigation.Preferences
import com.cedacri.arete.presentation.navigation.Reservations

enum class BottomBarType(val icon: ImageVector, val navKey: NavKey) {
    RESERVATIONS(bookmark_check, Reservations),
    OFFICE_MAP(map, OfficeMap),
    PREFERENCES(settings, Preferences)
}