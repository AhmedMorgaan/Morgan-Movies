package com.ds.morganmovies.feature_auth.presentation.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.ds.morganmovies.R
import com.ds.morganmovies.core.presentation.component.WelcomeButton
import com.ds.morganmovies.core.presentation.theme.Black_op
import com.ds.morganmovies.core.presentation.theme.Gray
import com.ds.morganmovies.core.presentation.theme.Red
import com.ds.morganmovies.core.presentation.theme.SephirFontFamily
import com.ds.morganmovies.core.presentation.theme.White
import com.ds.morganmovies.core.presentation.ui.Screen
import com.ds.morganmovies.core.utils.sdp
import com.ds.morganmovies.core.utils.ssp

@Composable
fun WelcomeScreen(navController: NavController){

    Box(
        Modifier
            .fillMaxSize()
            .background(Black_op)
            .padding(bottom = 50.sdp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 80.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome",
                fontSize = 30.ssp,
                fontFamily = SephirFontFamily,
                fontWeight = FontWeight.Bold,
                letterSpacing = 20.ssp
            )
            Spacer(modifier = Modifier.size(60.sdp))
            Image(
                painter = painterResource(id = R.drawable.logo_welcome), contentDescription = "",
                Modifier
                    .height(300.sdp)
                    .width(350.sdp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.sdp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WelcomeButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Login",
                borderStroke = BorderStroke(1.sdp, Red),
                backgroundColor = Red,
                contentColor = White,
                onClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
            Spacer(modifier = Modifier.size(15.sdp))
            WelcomeButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Sign Up",
                borderStroke = BorderStroke(1.sdp, Gray),
                backgroundColor = Black_op,
                contentColor = Red,
                onClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }


    }
}