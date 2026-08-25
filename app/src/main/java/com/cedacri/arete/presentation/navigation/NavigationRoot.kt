package com.cedacri.arete.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.cedacri.arete.data.local.datasource.TokenStorage
import com.cedacri.arete.presentation.screens.login.LoginScreen
import com.cedacri.arete.presentation.screens.officeManagement.OfficeManagementScreen
import com.cedacri.arete.presentation.screens.preferences.PreferencesScreen
import com.cedacri.arete.presentation.screens.seatReservation.OfficeMapScreen
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Composable
fun NavigationRoot(navigationManager: NavigationManager,
                   tokenStorage: TokenStorage) {
    var sessionChecked by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        val token =
            tokenStorage.getAuthToken()

        val tokenDate =
            tokenStorage.getTokenDate()

        val today =
            LocalDate.now().toString()

        if (
            token != null &&
            tokenDate == today
        ) {
            navigationManager.replaceScreen(
                OfficeMap
            )
        } else {
            if (token != null || tokenDate != null) {
                tokenStorage.clear()
            }
            navigationManager.replaceScreen(
                LoginScreen
            )
        }

        sessionChecked = true
    }

    if (!sessionChecked) {
        return
    }

    BackHandler(true) {
        navigationManager.goBack()
    }

    NavDisplay(
        backStack = navigationManager.backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            EnterTransition.None togetherWith ExitTransition.None
        },
        popTransitionSpec = {
            EnterTransition.None togetherWith ExitTransition.None
        },
        predictivePopTransitionSpec = {
            EnterTransition.None togetherWith ExitTransition.None
        },
        entryProvider = { key ->
            when (key) {
                is LoginScreen -> {
                    NavEntry(
                        key = key
                    ) {
                        LoginScreen()
                    }
                }

                is OfficeMap -> {
                    NavEntry(
                        key = key
                    ) {
                        OfficeMapScreen()
                    }
                }

                is Reservations -> {
                    NavEntry(
                        key = key
                    ) {
                        OfficeManagementScreen()
                    }
                }

                is Preferences -> {
                    NavEntry(
                        key = key
                    ) {
                        PreferencesScreen()
                    }
                }


                else -> throw RuntimeException("Invalid NavKey.")
            }
        }
    )
}

@Serializable
data object LoginScreen : NavKey

@Serializable
data object OfficeMap : NavKey

@Serializable
data object Reservations : NavKey

@Serializable
data object Preferences : NavKey