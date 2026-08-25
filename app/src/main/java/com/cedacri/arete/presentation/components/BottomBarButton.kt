package com.cedacri.arete.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.cedacri.arete.common.BottomBarType

@Composable
fun BottomBarButton(
    type: BottomBarType,
    selected: Boolean,
    onClick: () -> Unit
) {

    val scale by animateFloatAsState(
        if (selected) 1.15f else 1f,
        label = ""
    )

    val tint by animateColorAsState(
        if (selected)
            MaterialTheme.colorScheme.tertiary
        else
            Color.White,
        label = ""
    )

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = type.icon,
            contentDescription = type.name,
            tint = tint,
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
        )

    }
}