package com.cedacri.arete.presentation.screens.seatReservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cedacri.arete.domain.model.office.Obstacle
import com.cedacri.arete.domain.model.office.ObstacleType

@Composable
fun ObstacleCell(
    modifier: Modifier = Modifier,
    obstacle: Obstacle
) {

    val color = MaterialTheme.colorScheme.secondary.copy(0.5f)

    Box(

        modifier = modifier
            .background(
                color,
                RoundedCornerShape(8.dp)
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(8.dp)
            )
            .padding(4.dp),

        contentAlignment = Alignment.Center

    ) {

        Text(
            text = obstacle.label.ifBlank {
                obstacle.type.name
            },
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium
        )

    }

}