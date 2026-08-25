package com.cedacri.arete.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.cedacri.arete.common.BottomBarType
import com.cedacri.arete.presentation.navigation.LoginScreen
import com.cedacri.arete.presentation.navigation.NavigationManager
import org.koin.compose.koinInject
import kotlin.math.roundToInt

@Composable
fun FloatingBottomBar(
    modifier: Modifier = Modifier,
) {
    val navigation: NavigationManager = koinInject()

    var barWidthPx by remember {
        mutableIntStateOf(0)
    }

    val items = BottomBarType.entries.toList()

    if (navigation.currentScreen() is LoginScreen) return

    val selectedIndex = items.indexOfFirst {
        it.navKey == navigation.currentScreen()
    }.coerceAtLeast(0)

    val density = LocalDensity.current

    val itemWidthPx =
        if (items.isEmpty()) 0f
        else barWidthPx.toFloat() / items.size

    val indicatorSize = with(density) { 110.dp.toPx() }

    val indicatorOffset = remember {
        Animatable(0f)
    }

    LaunchedEffect(barWidthPx) {
        if (barWidthPx > 0) {
            indicatorOffset.snapTo(
                selectedIndex * itemWidthPx +
                        (itemWidthPx - indicatorSize) / 2f
            )
        }
    }

    LaunchedEffect(selectedIndex, itemWidthPx) {

        if (barWidthPx == 0) return@LaunchedEffect

        // Wait until the next frame so the destination screen
        // is already composed.
        withFrameNanos { }

        indicatorOffset.animateTo(
            targetValue =
                selectedIndex * itemWidthPx +
                        (itemWidthPx - indicatorSize) / 2f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .onSizeChanged {
                    barWidthPx = it.width
                },
            shape = RoundedCornerShape(36.dp),
            tonalElevation = 8.dp,
            shadowElevation = 12.dp
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.tertiary)
            ) {

                if (barWidthPx > 0) {

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .offset {
                                IntOffset(
                                    indicatorOffset.value.roundToInt(),
                                    0
                                )
                            }
                            .height(65.dp)
                            .width(110.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )

                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    items.forEachIndexed { index, item ->

                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {

                            BottomBarButton(
                                type = item,
                                selected = index == selectedIndex,
                                onClick = {
                                    navigation.navigateTo(item.navKey)
                                }
                            )

                        }

                    }

                }

            }

        }

    }
}