package com.cedacri.arete.presentation.screens.lockers.components

import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LockerTableHeader() {

    Row(
        modifier = Modifier
            .width(763.dp)
            .border(2.dp, MaterialTheme.colorScheme.outline),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = "Locker number",
            modifier = Modifier.width(140.dp)
                .padding(
                    vertical = 12.dp
                ),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )

        VerticalDivider(
            thickness = 1.dp,
            modifier = Modifier.height(60.dp),
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            text = "Assigned to",
            modifier = Modifier.width(240.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )

        VerticalDivider(
            thickness = 1.dp,
            modifier = Modifier.height(60.dp),
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            text = "Last updated at",
            modifier = Modifier.width(230.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )

        VerticalDivider(
            thickness = 1.dp,
            modifier = Modifier.height(60.dp),
            color = MaterialTheme.colorScheme.outline
        )

        Text(
            text = "Last updated by",
            modifier = Modifier.width(150.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
        )
    }
}