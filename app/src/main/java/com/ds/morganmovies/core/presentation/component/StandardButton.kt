
package com.ds.morganmovies.core.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ds.morganmovies.core.presentation.theme.Black_op
import com.ds.morganmovies.core.presentation.theme.ComicSansFontFamily
import com.ds.morganmovies.core.presentation.theme.MorganMoviesTheme
import com.ds.morganmovies.core.presentation.theme.Red
import com.ds.morganmovies.core.presentation.theme.Shapes
import com.ds.morganmovies.core.presentation.theme.White

@Composable
fun StandardButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = TextStyle(
        fontFamily = ComicSansFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Black_op
    ),
    shape: Shape = Shapes.large,
    backgroundColor: Color = Red,
    contentColor: Color = White,
    enabled: Boolean = true,
    disabledContentColor: Color = Red,
    disabledBackgroundColor: Color = Black_op,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    onClick: () -> Unit = {},
) {
    Button(
        modifier = modifier.height(50.dp),
        onClick = onClick,
        shape = shape,
        enabled = enabled,
        elevation = elevation,
        contentPadding = contentPadding,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            disabledContentColor = disabledContentColor,
            disabledBackgroundColor = disabledBackgroundColor
        ),
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = text,
            style = textStyle
        )
    }
}

@Preview
@Composable
fun TestButton() {
    MorganMoviesTheme {
        StandardButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Continue"
        )
    }
}