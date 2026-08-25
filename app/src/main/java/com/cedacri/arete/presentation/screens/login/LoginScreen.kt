package com.cedacri.arete.presentation.screens.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cedacri.arete.R
import com.cedacri.arete.common.scaleDp
import com.cedacri.arete.common.scaleSp
import com.cedacri.arete.presentation.components.PasswordField
import com.cedacri.arete.presentation.components.SignInButton
import com.cedacri.arete.presentation.components.UsernameField
import com.cedacri.arete.presentation.screens.seatReservation.SeatReservationEvent
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewmodel: LoginViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewmodel.events.collect { event ->
            when (event) {
                is LoginEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.cedacri_entrance),
            contentDescription = "Background photo",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            Modifier.fillMaxSize()
                .background(Color.Black.copy(0.5f))
        )

        Column(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.scaleDp())
                .padding(bottom = 120.scaleDp())
                .clip(RoundedCornerShape(24.scaleDp()))
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.scaleDp()))
                .border(width = 2.scaleDp(),
                        shape = RoundedCornerShape(24.scaleDp()),
                        color = MaterialTheme.colorScheme.secondary)
                .padding(24.scaleDp()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Welcome to Arete",
                textAlign = TextAlign.Center,
                fontSize = 28.scaleSp(),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = "Product designed to manage seat reservations",
                textAlign = TextAlign.Center,
                fontSize = 10.scaleSp(),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary.copy(0.77f)
            )

            Spacer(
                Modifier.height(5.scaleDp())
            )

            UsernameField(
                value = uiState.username,
                onValueChange = {
                    viewmodel.onIntent(LoginIntent.UsernameChanged(it))
                }
            )

            Spacer(
                Modifier.height(4.scaleDp())
            )

            PasswordField(
                value = uiState.password,
                visible = uiState.isPasswordVisible,
                onValueChange = {
                    viewmodel.onIntent(LoginIntent.PasswordChanged(it))
                },
                onVisibilityClick = {
                    viewmodel.onIntent(LoginIntent.TogglePasswordVisibility)
                }
            )

            Spacer(
                Modifier.height(8.scaleDp())
            )

            SignInButton {
                viewmodel.onIntent(LoginIntent.LoginClicked)
            }
        }
    }
}