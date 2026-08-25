package com.cedacri.arete.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.cedacri.arete.common.scaleDp
import com.cedacri.arete.common.scaleSp
import com.cedacri.arete.presentation.icons.password
import com.cedacri.arete.presentation.icons.visibility_off
import com.cedacri.arete.presentation.icons.visibility_on

@Composable
fun PasswordField(
    value: String,
    visible: Boolean,
    onValueChange: (String) -> Unit,
    onVisibilityClick: () -> Unit
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

    val passwordTransformation = remember {
        PasswordVisualTransformation()
    }

    val colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
        focusedLabelColor = MaterialTheme.colorScheme.tertiary,
        unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
        focusedLeadingIconColor = MaterialTheme.colorScheme.tertiary,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
        focusedTextColor = MaterialTheme.colorScheme.secondary,
        unfocusedTextColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
        focusedSupportingTextColor = MaterialTheme.colorScheme.secondary,
        unfocusedSupportingTextColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
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
            visualTransformation = if (visible) {
                VisualTransformation.None
            } else {
                passwordTransformation
            },

            textStyle = textStyle,
            shape = shape,

            label = { Text("Password") },

            leadingIcon = {
                Icon(
                    imageVector = password,
                    contentDescription = "Password Icon"
                )
            },
            trailingIcon = {
                IconButton(onClick = onVisibilityClick) {
                    if (visible) {
                        Icon(
                            imageVector = visibility_on,
                            contentDescription = "Visibility on button"
                        )
                    } else {
                        Icon(
                            imageVector = visibility_off,
                            contentDescription = "Visibility off button"
                        )
                    }

                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrectEnabled = false
            ),


            colors = colors,
            singleLine = true
        )
    }
}