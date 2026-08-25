package com.cedacri.arete.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.cedacri.arete.common.scaleDp
import com.cedacri.arete.common.scaleSp

@Composable
fun SignInButton(
    onClick: () -> Unit
) {
    Text(
        text = "Sign In",
        textAlign = TextAlign.Center,
        fontSize = 18.scaleSp(),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(12.scaleDp()))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = MaterialTheme.colorScheme.primary),
                onClick = onClick
            )
            .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.scaleDp()))
            .padding(horizontal = 32.scaleDp(), vertical = 8.scaleDp())
    )
}