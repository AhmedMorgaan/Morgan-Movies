package com.ds.morganmovies.core.presentation.component

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.ds.morganmovies.core.presentation.theme.Black_op
import com.ds.morganmovies.core.presentation.theme.ComicSansFontFamily
import com.ds.morganmovies.core.presentation.theme.Gray
import com.ds.morganmovies.core.presentation.theme.Red
import com.ds.morganmovies.core.presentation.theme.SemiGray
import com.ds.morganmovies.core.utils.sdp
import com.ds.morganmovies.core.utils.ssp
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@Preview(showBackground = true)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StanderOutlineTextFiled(
    modifier: Modifier = Modifier,
    text: String = "",
    label: String = "",
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    hasNext: Boolean = false,
    isPasswordField: Boolean = false,
    singleLine: Boolean = true,
    maxLine: Int = 1,
    focusRequester: FocusRequester = FocusRequester()

) {

    val  coroutineScope = rememberCoroutineScope()
    val  bringIntoViewRequester = BringIntoViewRequester()
    val isPasswordVisible = remember { mutableStateOf(false) }
    val localFocusManger = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier
            .focusRequester(focusRequester = focusRequester)
            .fillMaxWidth()
            .height(50.sdp)
            .bringIntoViewRequester(bringIntoViewRequester)
            .onFocusEvent {
                if (it.isFocused || it.hasFocus) {
                    coroutineScope.launch {
                        delay(250)
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            },
        value = text,
        onValueChange = { onValueChange(it) },
        label = {
            Text(
                text = label,
                fontFamily = ComicSansFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.ssp,
                maxLines = maxLine
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = capitalization,
            imeAction = if (!singleLine) {
                ImeAction.Default
            } else {
                if (hasNext) ImeAction.Next else ImeAction.Done
            }
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManger.clearFocus()
            },
            onNext = {
                localFocusManger.moveFocus(FocusDirection.Next)
            }
        ),
        visualTransformation = if (!isPasswordVisible.value && isPasswordField) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        singleLine = singleLine,
        maxLines = maxLine,
        shape = RoundedCornerShape(30),
        textStyle = TextStyle(fontFamily = ComicSansFontFamily, fontSize = 12.ssp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Red,
            unfocusedBorderColor = Gray,
            backgroundColor = Black_op,
            focusedLabelColor = Red,
            unfocusedLabelColor = Red,
            textColor = SemiGray,
            cursorColor = Red,

        ),
        trailingIcon = if (isPasswordField) {
            {
                IconButton(onClick = {
                    isPasswordVisible.value = !isPasswordVisible.value
                }) {
                    Icon(
                        imageVector = if(isPasswordVisible.value) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        tint = Color.Gray,
                        contentDescription =""
                    )
                }
            }

        } else null,


    )

}

fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    if (isFocused) {
        val imeIsVisible = LocalWindowInsets.current.ime.isVisible
        val focusManager = LocalFocusManager.current
        Log.e("imeIsVisible1", "clearFocusOnKeyboardDismiss: $imeIsVisible")
        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                Log.e("imeIsVisible2", "clearFocusOnKeyboardDismiss: $imeIsVisible")
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}