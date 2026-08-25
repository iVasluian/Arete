package com.cedacri.arete.presentation.screens.seatReservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cedacri.arete.domain.model.office.OfficeGroup
import com.cedacri.arete.domain.model.office.Seat
import androidx.core.graphics.toColorInt
import com.cedacri.arete.presentation.icons.add
import com.cedacri.arete.presentation.icons.add_circle

@Composable
fun SeatCell(
    modifier: Modifier = Modifier,
    seat: Seat,
    group: OfficeGroup?,
    canReserve: Boolean,
    availableSeatColor: Color,
    onClick: () -> Unit
) {

    val background = when {

        seat.isReserved && group != null ->
            parseGroupColor(group.color)

        availableSeatColor == parseGroupColor("#1A000000") && canReserve ->
            MaterialTheme.colorScheme.secondary.copy(0.1f)

        canReserve ->
            availableSeatColor

        else ->
            MaterialTheme.colorScheme.secondary.copy(0.1f)
    }

    Box(

        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(8.dp)
            )
            .clickable(
                enabled = canReserve || seat.reservedBy != null,
                onClick = onClick
            ),

        contentAlignment = Alignment.Center

    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



            if (canReserve) {

                Icon(
                    imageVector = add_circle,
                    contentDescription = "Reserve seat",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = seat.name.substringAfter("-"),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall
                )

                seat.reservedBy?.let {

                    Text(
                        text = it.fullName
                            .split(" ")
                            .map { name -> name.first() }
                            .joinToString(""),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                }
            }

        }

    }

}

fun parseGroupColor(
    color: String
): Color {

    return runCatching {
        Color(
            color.toColorInt()
        )
    }.getOrDefault(
        Color.Gray
    )
}