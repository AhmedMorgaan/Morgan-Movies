
package com.ds.morganmovies.core.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(12.dp)
)
val BottomCardShape = Shapes(
    large = RoundedCornerShape(
        topStart = 80.dp
    )
)