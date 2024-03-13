
package com.ds.morganmovies.core.presentation.component

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.morganmovies.R
import com.ds.morganmovies.core.presentation.theme.Black_op
import com.ds.morganmovies.core.presentation.theme.ComicSansFontFamily
import com.ds.morganmovies.core.presentation.theme.Gray
import com.ds.morganmovies.core.presentation.theme.MorganMoviesTheme
import com.ds.morganmovies.core.presentation.theme.Red
import com.ds.morganmovies.core.presentation.theme.SemiGray
import com.ds.morganmovies.core.utils.sdp
import com.ds.morganmovies.core.utils.ssp
import java.util.*

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    dateText: String? = null,
    isHintVisible: Boolean = true,
    hint: String = " DD / MM / YYYY",
    selectedDate: Triple<Int, Int, Int> = Triple(
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    ),
    icon: Int = R.drawable.ic_calendar,
    showIcon: Boolean = false,
    textFieldSize: Size = Size.Zero,
    dateEntered: (Int, Int, Int) -> Unit,
    disablePreviousDate: Boolean = false,
) {
    val datePickerDialog = datePickerDialog(dateEntered, selectedDate, disablePreviousDate)
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth().height(45.sdp),
        onClick = {
            datePickerDialog.show()
        },
        shape = RoundedCornerShape(30),
        border = BorderStroke(width = 1.dp, color = Gray),
        colors = ButtonDefaults.buttonColors(backgroundColor = Black_op)
    ) {
        if (showIcon) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(8.sdp))
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = if (isHintVisible) hint else dateText ?: "",
            style = if (isHintVisible) {
                TextStyle(fontFamily = ComicSansFontFamily, fontSize = 12.ssp, color = Red)
            } else {
                TextStyle(fontFamily = ComicSansFontFamily, fontSize = 12.ssp, color = SemiGray)
            }
        )
    }
}

@Composable
fun datePickerDialog(
    dateEntered: (Int, Int, Int) -> Unit,
    selectedDate: Triple<Int, Int, Int> = Triple(
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    ),
    disablePreviousDate: Boolean = false,
): DatePickerDialog {
    val datePickerDialog = DatePickerDialog(
        LocalContext.current, { _: DatePicker, year: Int, month: Int, day: Int ->
            dateEntered(year, month, day)
        }, selectedDate.first, selectedDate.second, selectedDate.third
    )
    if (disablePreviousDate)
        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
    return datePickerDialog
}

@Preview
@Composable
fun TestDatePicker() {
    MorganMoviesTheme {
        DatePicker(
            modifier = Modifier.height(50.dp),
            showIcon = true,
            selectedDate = Triple(0, 0, 0),
            dateEntered = { _, _, _ ->
            },
        )
    }
}