package com.cedacri.arete.common

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun getDeviceSize(): DeviceSize {
    return when {
        LocalConfiguration.current.screenWidthDp > 599 -> DeviceSize.TABLET

        LocalConfiguration.current.screenWidthDp < 365 -> DeviceSize.SMALL_DEVICE

        else -> DeviceSize.DEFAULT
    }
}

@Composable
fun isTablet(): Boolean {
    return getDeviceSize() == DeviceSize.TABLET
}

@Composable
fun isSmallDevice(): Boolean {
    return getDeviceSize() == DeviceSize.SMALL_DEVICE
}

enum class DeviceSize {
    TABLET,
    SMALL_DEVICE,
    DEFAULT
}

@Composable
fun Float.scaleDp(): Dp {
    return if (isTablet()) {
        (this / 0.53f).dp
    } else if (isSmallDevice()) {
        (this * 0.93f).dp
    } else {
        this.dp
    }
}

@Composable
fun Float.scaleSp(): TextUnit {
    return if (isTablet()) {
        (this / 0.53f).sp
    } else if (isSmallDevice()) {
        (this * 0.93f).sp
    } else {
        this.sp
    }
}

@Composable
fun Int.scaleDp(): Dp {
    return this.toFloat().scaleDp()
}

@Composable
fun Int.scaleSp(): TextUnit {
    return this.toFloat().scaleSp()
}