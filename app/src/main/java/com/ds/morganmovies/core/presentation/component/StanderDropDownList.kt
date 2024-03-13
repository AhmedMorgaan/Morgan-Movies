package com.ds.morganmovies.core.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.ds.morganmovies.core.presentation.theme.Black_op
import com.ds.morganmovies.core.presentation.theme.ComicSansFontFamily
import com.ds.morganmovies.core.presentation.theme.Gray
import com.ds.morganmovies.core.presentation.theme.Red
import com.ds.morganmovies.core.utils.sdp
import com.ds.morganmovies.core.utils.ssp

@Composable
fun StandardDropDownMenu(
    modifier: Modifier = Modifier,
    text: String = "",
    onValueChange: (String) -> Unit = {},
    shape: Shape = RoundedCornerShape(30),
    list: List<String> = emptyList(),
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var buttonWidth by remember {
        mutableStateOf(Size.Zero)
    }
    Column(modifier = modifier) {
        OutlinedButton(
            modifier = modifier.onGloballyPositioned {
                buttonWidth = it.size.toSize()
            }.height(45.sdp),
            onClick = { expanded = !expanded },
            shape = shape,
            border = BorderStroke(width = 1.dp, color = Gray),
            colors = ButtonDefaults.buttonColors(backgroundColor = Black_op)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = text,
                    style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = ComicSansFontFamily,
                        fontSize = 12.ssp,
                        color = Red
                    )
                )
                Icon(
                    imageVector = if (expanded) {
                        Icons.Filled.KeyboardArrowUp
                    } else {
                        Icons.Filled.KeyboardArrowDown
                    },
                    tint = Color.Gray,
                    contentDescription = ""
                )
            }
        }
        DropdownMenu(
            modifier = Modifier
                .width(with(LocalDensity.current) {
                    buttonWidth.width.toDp()
                })
                .background(MaterialTheme.colors.background),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            list.forEach { label ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onValueChange(label)
                }) {
                    Text(
                        text = label,
                        style = TextStyle(
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = ComicSansFontFamily,
                            fontSize = 12.ssp,
                            color = Red
                        )
                    )
                }
            }
        }
    }
}