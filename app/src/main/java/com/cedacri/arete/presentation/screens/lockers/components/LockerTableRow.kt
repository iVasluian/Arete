package com.cedacri.arete.presentation.screens.lockers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cedacri.arete.domain.model.office.Locker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun LockerTableRow(
    color: Color,
    locker: Locker
) {
    val updatedAtFormatter =
        DateTimeFormatter.ofPattern("yyyy MMM dd")

    Row(
        modifier = Modifier
            .width(763.dp)
            .background(color),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text =
                "${locker.officeName} - " +
                        locker.name,
            modifier = Modifier.width(140.dp)
                .padding(
                    vertical = 12.dp
                ),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )

        VerticalDivider(
            thickness = 1.dp,
            modifier = Modifier.height(60.dp),
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            text =
                locker.employeeId?.let { id ->

                    val name =
                        locker.employeeName
                            ?: ""

                    "$id - $name"

                } ?: "",
            modifier = Modifier.width(240.dp),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )

        VerticalDivider(
            thickness = 1.dp,
            modifier = Modifier.height(60.dp),
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            text =
                locker.updatedAt
                    ?.let {
                        LocalDateTime.parse(it)
                            .format(updatedAtFormatter)
                    }
                    ?: "",
            modifier = Modifier.width(230.dp),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )

        VerticalDivider(
            thickness = 1.dp,
            modifier = Modifier.height(60.dp),
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            text =
                locker.updatedBy ?: "",
            modifier = Modifier.width(150.dp),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )
    }
}