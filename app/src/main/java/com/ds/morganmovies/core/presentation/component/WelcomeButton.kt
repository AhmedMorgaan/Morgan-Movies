package com.ds.morganmovies.core.presentation.component


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.ds.morganmovies.core.presentation.theme.ComicSansFontFamily
import com.ds.morganmovies.core.utils.sdp
import com.ds.morganmovies.core.utils.ssp

@Composable
fun WelcomeButton(
    modifier: Modifier = Modifier,
    text: String,
    borderStroke: BorderStroke ,
    backgroundColor: Color,
    contentColor:Color,
    onClick: () -> Unit = {}
) {

    Button(
        modifier = modifier,
        border = borderStroke,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        shape = RoundedCornerShape(30)
    ) {
        Text(
            text = text,
            fontSize = 15.ssp,
            fontFamily = ComicSansFontFamily,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.height(30.sdp)
        )
    }
}