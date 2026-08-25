package com.cedacri.arete.presentation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val password: ImageVector
    get() {
        if (_password != null) {
            return _password!!
        }
        _password =
            ImageVector.Builder(
                name = "password_2",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(4f, 13f)
                        quadTo(2.75f, 13f, 1.88f, 12.13f)
                        reflectiveQuadTo(1f, 10f)
                        reflectiveQuadTo(1.88f, 7.88f)
                        reflectiveQuadTo(4f, 7f)
                        reflectiveQuadTo(6.13f, 7.88f)
                        reflectiveQuadTo(7f, 10f)
                        reflectiveQuadTo(6.13f, 12.13f)
                        reflectiveQuadTo(4f, 13f)
                        close()
                        moveTo(2f, 19f)
                        verticalLineTo(17f)
                        horizontalLineTo(22f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(2f)
                        close()
                        moveTo(9.88f, 12.13f)
                        quadTo(9f, 11.25f, 9f, 10f)
                        reflectiveQuadTo(9.88f, 7.88f)
                        reflectiveQuadTo(12f, 7f)
                        reflectiveQuadToRelative(2.13f, 0.88f)
                        reflectiveQuadTo(15f, 10f)
                        reflectiveQuadToRelative(-0.88f, 2.13f)
                        reflectiveQuadTo(12f, 13f)
                        reflectiveQuadTo(9.88f, 12.13f)
                        close()
                        moveToRelative(8f, 0f)
                        quadTo(17f, 11.25f, 17f, 10f)
                        reflectiveQuadTo(17.88f, 7.88f)
                        reflectiveQuadTo(20f, 7f)
                        reflectiveQuadToRelative(2.13f, 0.88f)
                        reflectiveQuadTo(23f, 10f)
                        reflectiveQuadToRelative(-0.88f, 2.13f)
                        reflectiveQuadTo(20f, 13f)
                        reflectiveQuadTo(17.88f, 12.13f)
                        close()
                    }
                }
                .build()
        return _password!!
    }

private var _password: ImageVector? = null