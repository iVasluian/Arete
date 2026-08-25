package com.cedacri.arete.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.cedacri.arete.common.scaleDp
import com.cedacri.arete.common.scaleSp
import com.cedacri.arete.presentation.icons.close
import com.cedacri.arete.presentation.icons.username

@Composable
fun UsernameField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val maxLength = 50

    val sdp16 = 16.scaleDp()
    val ssp16 = 16.scaleSp()
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val shape = remember { RoundedCornerShape(sdp16) }
    val textStyle = remember {
        TextStyle(
            fontSize = ssp16,
            color = secondaryColor
        )
    }

    val colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.tertiary,      // Deep premium purple
        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,    // Elegant sub-neutral gray
        focusedLabelColor = MaterialTheme.colorScheme.tertiary,       // Label color when active
        unfocusedLabelColor = MaterialTheme.colorScheme.secondary,     // Label color when idle
        focusedLeadingIconColor = MaterialTheme.colorScheme.tertiary,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary.copy(0.5f),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
        focusedTextColor = MaterialTheme.colorScheme.secondary,
        unfocusedTextColor = MaterialTheme.colorScheme.secondary.copy(0.5f),
        focusedSupportingTextColor = MaterialTheme.colorScheme.secondary,
        unfocusedSupportingTextColor = MaterialTheme.colorScheme.secondary.copy(0.5f),
        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary.copy(0.5f),
        cursorColor = MaterialTheme.colorScheme.secondary
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),

            textStyle = textStyle,
            shape = shape,

            label = { Text("Username") },
            placeholder = { Text("crmx123") },

            leadingIcon = {
                Icon(
                    imageVector = username,
                    contentDescription = "Username Icon"
                )
            },
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = close,
                            contentDescription = "Clear text"
                        )
                    }
                }
            },

            colors = colors,
            singleLine = true
        )
    }
}