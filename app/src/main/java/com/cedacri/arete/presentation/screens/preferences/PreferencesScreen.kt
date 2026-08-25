package com.cedacri.arete.presentation.screens.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cedacri.arete.data.local.datasource.TokenStorage
import com.cedacri.arete.presentation.navigation.NavigationManager
import com.cedacri.arete.presentation.screens.preferences.components.AppearanceSection
import com.cedacri.arete.presentation.screens.preferences.components.AvailableSeatColorSection
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun PreferencesScreen(
    tokenStorage: TokenStorage = koinInject(),
    viewModel: PreferencesViewModel = koinViewModel(),
    navigation: NavigationManager = koinInject()
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(24.dp)
    ) {

        Text(
            text = "Preferences",
            style =
                MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        AppearanceSection(
            appearance = uiState.appearance,
            onAppearanceChanged = {
                viewModel.setAppearance(it)
            }
        )

        AvailableSeatColorSection(
            color = uiState.availableSeatColor,
            onColorChanged = {
                viewModel.setAvailableSeatColor(it)
            }
        )

        Spacer(
            Modifier.weight(1f)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
            onClick = {
                scope.launch {
                    tokenStorage.clear()
                    navigation.goBackHome()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(
                text = "Log out",
                color = Color.White
            )
        }

        Spacer(
            Modifier.height(75.dp)
        )
    }
}