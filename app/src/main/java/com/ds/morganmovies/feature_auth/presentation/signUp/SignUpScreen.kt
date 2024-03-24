package com.ds.morganmovies.feature_auth.presentation.signUp

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.ds.morganmovies.R
import com.ds.morganmovies.core.presentation.component.DatePicker
import com.ds.morganmovies.core.presentation.component.StandardButton
import com.ds.morganmovies.core.presentation.component.StandardDropDownMenu
import com.ds.morganmovies.core.presentation.component.StanderOutlineTextFiled
import com.ds.morganmovies.core.presentation.theme.ComicSansFontFamily
import com.ds.morganmovies.core.presentation.theme.Red
import com.ds.morganmovies.core.presentation.theme.SephirFontFamily
import com.ds.morganmovies.core.utils.registerViewingDate
import com.ds.morganmovies.core.utils.rememberImeState
import com.ds.morganmovies.core.utils.sdp
import com.ds.morganmovies.core.utils.ssp
import com.google.accompanist.insets.imePadding
import java.util.Calendar

@SuppressLint("SuspiciousIndentation")
@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current

    val firstName = remember {
        mutableStateOf("")
    }
    val lastName = remember {
        mutableStateOf("")
    }
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val phone = remember {
        mutableStateOf("")
    }
//    val date: MutableState<Triple<Int, Int, Int>> = remember {
//        mutableStateOf(Triple(0,0,0))
//    }

    val date = remember {
        mutableStateOf("")
    }

    val selectedDate: MutableState<Triple<Int, Int, Int>> = remember {
        mutableStateOf(Triple(0,0,0))
    }
    val isHintVisible = remember {
        mutableStateOf(true)
    }

    val genderList = listOf("Choose Gender ...","Male","Female")
    val gender = remember {
        mutableStateOf(genderList[0])
    }

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()



    LaunchedEffect(key1 = imeState.value ){
        if(imeState.value){
            scrollState.scrollTo(scrollState.viewportSize)
        }
    }

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 50.sdp, start = 20.sdp, end = 20.sdp, bottom = 30.sdp)
                .verticalScroll(scrollState)
                //.navigationBarsWithImePadding()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                fontFamily = SephirFontFamily,
                fontSize = 40.ssp,
                color = Color(0xffe84755),
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.ssp
            )
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(data = R.drawable.sign_up_gif)
                        .apply(block = {
                            size(Size.ORIGINAL)
                        }).build(), imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.size(20.sdp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StanderOutlineTextFiled(
                    modifier = Modifier.weight(1f),
                    onValueChange = {
                        firstName.value = it
                    },
                    text = firstName.value,
                    label = stringResource(R.string.first_name),
                    hasNext = true
                )
                Spacer(modifier = Modifier.size(10.sdp))
                StanderOutlineTextFiled(
                    modifier = Modifier.weight(1f),
                    onValueChange = {
                                    lastName.value = it
                    },
                    text = lastName.value,
                    label = stringResource(R.string.last_name),
                    hasNext = true
                )
            }
            Spacer(modifier = Modifier.size(10.sdp))
            StanderOutlineTextFiled(
                onValueChange = {
                    email.value = it
                },
                text = email.value,
                label = stringResource(R.string.Email),
                hasNext = true,
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.size(10.sdp))
            StanderOutlineTextFiled(
                onValueChange = {
                                password.value = it
                },
                text = password.value,
                label = stringResource(R.string.password),
                hasNext = true,
                isPasswordField = true,
                keyboardType = KeyboardType.Password
            )
            Spacer(modifier = Modifier.size(10.dp))
            StanderOutlineTextFiled(
                onValueChange = {
                    phone.value = it
                },
                text = phone.value,
                label = stringResource(R.string.phone_number),
                hasNext = false,
                keyboardType = KeyboardType.Phone
            )
            Spacer(modifier = Modifier.size(10.dp))
            StandardDropDownMenu(
                text = gender.value,
                onValueChange = {
                                gender.value = it
                },
                list = genderList
            )
            Spacer(modifier = Modifier.size(10.sdp))
            Text(
                modifier = Modifier.align(Start),
                text = stringResource(R.string.date_of_birth),
                style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = ComicSansFontFamily,
                    fontSize = 12.ssp,
                    color = Red
                ),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.size(5.sdp))
            val instance = Calendar.getInstance()
            DatePicker(
                dateText = date.value,
                selectedDate = selectedDate.value,
                isHintVisible = isHintVisible.value,
                dateEntered = { year, month, day ->
                    isHintVisible.value = false
                    val dateEntered = Triple(year,month,day)
                    date.value = instance.registerViewingDate(year,month, day)
                    selectedDate.value = dateEntered
                },
                showIcon = true
            )
            Spacer(modifier = Modifier.size(20.sdp))
            StandardButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.Continue)
            )

        }
    }
