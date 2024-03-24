package com.ds.morganmovies.feature_auth.presentation.login

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.ds.morganmovies.R
import com.ds.morganmovies.core.presentation.component.StandardButton
import com.ds.morganmovies.core.presentation.component.StanderOutlineTextFiled
import com.ds.morganmovies.core.presentation.theme.Red
import com.ds.morganmovies.core.presentation.theme.SephirFontFamily
import com.ds.morganmovies.core.presentation.theme.comicStyleBold
import com.ds.morganmovies.core.utils.sdp
import com.ds.morganmovies.core.utils.ssp
import com.google.accompanist.insets.imePadding

@Composable
fun LoginScreen(navController: NavController) {

    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()


    Box(modifier = Modifier.fillMaxSize()){
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(top = 50.sdp, start = 20.sdp, end = 20.sdp, bottom = 30.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = stringResource(R.string.login),
                fontFamily = SephirFontFamily,
                fontSize = 40.ssp,
                color = Color(0xffe84755),
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.ssp
            )
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(data = R.drawable.login_gif)
                        .apply(block = {
                            size(Size.ORIGINAL)
                        }).build(), imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(250.sdp)
                    .fillMaxWidth(),
            )
            StanderOutlineTextFiled(
                onValueChange = {
                    email.value = it
                },
                text = email.value,
                label = stringResource(R.string.Email),
                keyboardType = KeyboardType.Email,
            )
            Spacer(modifier = Modifier.size(10.sdp))
            StanderOutlineTextFiled(
                onValueChange = {
                    password.value = it
                },
                text = password.value,
                label = stringResource(R.string.password),
                keyboardType = KeyboardType.Password,
                isPasswordField = true,
            )
            Spacer(modifier = Modifier.size(20.sdp))
            TextButton(
                onClick = { 
                    
                }
            ) {
                Text(
                    text = stringResource(R.string.forget_your_password),
                    style = comicStyleBold,
                    fontSize = 12.ssp,
                    color = Red
                )
            }
            Spacer(modifier = Modifier.size(20.sdp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription ="",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.sdp)
                        .clip(CircleShape)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription ="",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.sdp)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.size(30.sdp))
            StandardButton(
                text = stringResource(R.string.login_small),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}