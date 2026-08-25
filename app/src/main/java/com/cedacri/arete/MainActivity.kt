package com.cedacri.arete

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cedacri.arete.data.local.datasource.TokenStorage
import com.cedacri.arete.domain.model.preferences.Appearance
import com.cedacri.arete.presentation.components.FloatingBottomBar
import com.cedacri.arete.presentation.navigation.NavigationRoot
import com.cedacri.arete.presentation.navigation.NavigationManager
import com.cedacri.arete.presentation.theme.AreteTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            val navigationVM: NavigationManager = koinInject()
            val tokenStorage: TokenStorage = koinInject()

            val appearance by tokenStorage
                .getAppearanceFlow()
                .collectAsState(
                    initial = Appearance.SYSTEM
                )

            val darkTheme = when (appearance) {
                Appearance.SYSTEM ->
                    isSystemInDarkTheme()

                Appearance.LIGHT ->
                    false

                Appearance.DARK ->
                    true
            }

            AreteTheme(darkTheme = darkTheme){
                NavigationRoot(navigationVM, tokenStorage)

                FloatingBottomBar(
                    Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}