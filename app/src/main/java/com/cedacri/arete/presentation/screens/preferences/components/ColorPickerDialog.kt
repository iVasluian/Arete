package com.cedacri.arete.presentation.screens.preferences.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.roundToInt

@Composable
fun ColorPickerDialog(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {

    var selectedColor by remember {
        mutableStateOf(initialColor)
    }

    var alpha by remember {
        mutableFloatStateOf(initialColor.alpha)
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surface
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Choose color",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                ColorSpectrum(
                    initialColor = initialColor,
                    onColorChanged = {
                        selectedColor = it.copy(
                            alpha = alpha
                        )
                    }
                )

                AlphaPicker(
                    color = selectedColor,
                    alpha = alpha,
                    onAlphaChanged = { newAlpha ->

                        alpha = newAlpha

                        selectedColor =
                            selectedColor.copy(
                                alpha = newAlpha
                            )
                    }
                )


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                        .background(selectedColor)
                )

                Text(
                    text = selectedColor.toHex(),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.End
                ) {

                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancel",
                            color = MaterialTheme.colorScheme.secondary)
                    }

                    Button(
                        onClick = {
                            onColorSelected(selectedColor)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text("Apply",
                            color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSpectrum(
    initialColor: Color,
    onColorChanged: (Color) -> Unit
) {

    val hsv = remember(initialColor) {
        initialColor.toHsv()
    }

    var hue by remember {
        mutableFloatStateOf(hsv[0])
    }

    var saturation by remember {
        mutableFloatStateOf(hsv[1])
    }

    var value by remember {
        mutableFloatStateOf(hsv[2])
    }

    Column(
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {

        SaturationValuePicker(
            hue = hue,
            saturation = saturation,
            value = value,
            onChanged = { newSaturation, newValue ->

                saturation = newSaturation
                value = newValue

                onColorChanged(
                    Color.hsv(
                        hue,
                        saturation,
                        value
                    )
                )
            }
        )

        HuePicker(
            hue = hue,
            onHueChanged = { newHue ->

                hue = newHue

                onColorChanged(
                    Color.hsv(
                        hue,
                        saturation,
                        value
                    )
                )
            }
        )
    }
}

@Composable
private fun SaturationValuePicker(
    hue: Float,
    saturation: Float,
    value: Float,
    onChanged: (Float, Float) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(
                RoundedCornerShape(12.dp)
            )
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val hueColor =
                Color.hsv(
                    hue,
                    1f,
                    1f
                )

            drawRect(
                color = hueColor
            )

            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.White,
                        Color.Transparent
                    )
                )
            )

            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black
                    )
                )
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(hue) {

                    detectDragGestures { change, _ ->

                        change.consume()

                        val newSaturation =
                            (
                                    change.position.x /
                                            size.width
                                    ).coerceIn(0f, 1f)

                        val newValue =
                            (
                                    1f -
                                            change.position.y /
                                            size.height
                                    ).coerceIn(0f, 1f)

                        onChanged(
                            newSaturation,
                            newValue
                        )
                    }
                }
        ) {

            val x =
                saturation * size.width

            val y =
                (1f - value) * size.height

            drawCircle(
                color = Color.White,
                radius = 10.dp.toPx(),
                center = Offset(x, y),
                style = Stroke(
                    width = 2.dp.toPx()
                )
            )

            drawCircle(
                color = Color.Black,
                radius = 7.dp.toPx(),
                center = Offset(x, y),
                style = Stroke(
                    width = 1.dp.toPx()
                )
            )
        }
    }
}

@Composable
private fun HuePicker(
    hue: Float,
    onHueChanged: (Float) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .clip(
                RoundedCornerShape(14.dp)
            )
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Red,
                        Color.Yellow,
                        Color.Green,
                        Color.Cyan,
                        Color.Blue,
                        Color.Magenta,
                        Color.Red
                    )
                )
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {

                    detectDragGestures(
                        onDragStart = { offset ->

                            val newHue =
                                (
                                        offset.x /
                                                size.width
                                        ) * 360f

                            onHueChanged(
                                newHue.coerceIn(
                                    0f,
                                    360f
                                )
                            )
                        },
                        onDrag = { change, _ ->

                            change.consume()

                            val newHue =
                                (
                                        change.position.x /
                                                size.width
                                        ) * 360f

                            onHueChanged(
                                newHue.coerceIn(
                                    0f,
                                    360f
                                )
                            )
                        }
                    )
                }
        ) {

            val x =
                hue / 360f * size.width

            drawCircle(
                color = Color.White,
                radius = 10.dp.toPx(),
                center = Offset(
                    x,
                    size.height / 2
                ),
                style = Stroke(
                    width = 2.dp.toPx()
                )
            )

            drawCircle(
                color = Color.Black,
                radius = 7.dp.toPx(),
                center = Offset(
                    x,
                    size.height / 2
                ),
                style = Stroke(
                    width = 1.dp.toPx()
                )
            )
        }
    }
}

fun Color.toHex(): String {

    return String.format(
        "#%02X%02X%02X%02X",
        (alpha * 255).roundToInt(),
        (red * 255).roundToInt(),
        (green * 255).roundToInt(),
        (blue * 255).roundToInt()
    )
}

private fun Color.toHsv(): FloatArray {

    val hsv = FloatArray(3)

    android.graphics.Color.RGBToHSV(
        (red * 255).roundToInt(),
        (green * 255).roundToInt(),
        (blue * 255).roundToInt(),
        hsv
    )

    return hsv
}

@Composable
private fun AlphaPicker(
    color: Color,
    alpha: Float,
    onAlphaChanged: (Float) -> Unit
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(6.dp)
    ) {

        Text(
            text = "Opacity",
            style =
                MaterialTheme.typography.bodyMedium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .clip(
                    RoundedCornerShape(14.dp)
                )
        ) {

            AlphaCheckerboard()

            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {

                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            color.copy(alpha = 0f),
                            color.copy(alpha = 1f)
                        )
                    )
                )
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {

                        detectDragGestures(
                            onDragStart = { offset ->

                                val newAlpha =
                                    (
                                            offset.x /
                                                    size.width
                                            ).coerceIn(
                                            0f,
                                            1f
                                        )

                                onAlphaChanged(
                                    newAlpha
                                )
                            },
                            onDrag = { change, _ ->

                                change.consume()

                                val newAlpha =
                                    (
                                            change.position.x /
                                                    size.width
                                            ).coerceIn(
                                            0f,
                                            1f
                                        )

                                onAlphaChanged(
                                    newAlpha
                                )
                            }
                        )
                    }
            ) {

                val x =
                    alpha * size.width

                drawCircle(
                    color = Color.White,
                    radius = 10.dp.toPx(),
                    center = Offset(
                        x,
                        size.height / 2
                    ),
                    style = Stroke(
                        width = 2.dp.toPx()
                    )
                )

                drawCircle(
                    color = Color.Black,
                    radius = 7.dp.toPx(),
                    center = Offset(
                        x,
                        size.height / 2
                    ),
                    style = Stroke(
                        width = 1.dp.toPx()
                    )
                )
            }
        }

        Text(
            text = "${(alpha * 100).roundToInt()}%",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style =
                MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun AlphaCheckerboard() {

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        val squareSize =
            8.dp.toPx()

        val columns =
            (size.width / squareSize)
                .toInt() + 1

        val rows =
            (size.height / squareSize)
                .toInt() + 1

        for (row in 0 until rows) {

            for (column in 0 until columns) {

                val isDark =
                    (row + column) % 2 == 0

                drawRect(
                    color =
                        if (isDark) {
                            Color.LightGray
                        } else {
                            Color.White
                        },
                    topLeft = Offset(
                        column * squareSize,
                        row * squareSize
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        squareSize,
                        squareSize
                    )
                )
            }
        }
    }
}