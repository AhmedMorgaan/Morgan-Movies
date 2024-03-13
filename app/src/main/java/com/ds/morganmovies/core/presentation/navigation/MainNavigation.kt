package com.ds.morganmovies.core.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ds.morganmovies.core.presentation.ui.Screen
import com.ds.morganmovies.core.utils.findActivity
import com.ds.morganmovies.feature_auth.presentation.login.LoginScreen
import com.ds.morganmovies.feature_auth.presentation.onboarding.OnBoardingScreen
import com.ds.morganmovies.feature_auth.presentation.onboarding.WelcomeScreen
import com.ds.morganmovies.feature_auth.presentation.signUp.SignUpScreen
import com.ds.morganmovies.feature_auth.presentation.splash.SplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = {
            EnterTransition.None
        },
    exitTransition = {
        ExitTransition.None
    }) {
        composable(Screen.Splash.route){
            SplashScreen(navController)
        }
        composable(Screen.OnBoarding.route){
            OnBoardingScreen(navController)
        }
        composable(Screen.Login.route){
            LoginScreen(navController)
        }
        composable(Screen.Welcome.route){
            WelcomeScreen(navController)
        }
        composable(Screen.SignUp.route){
            SignUpScreen(navController)
        }
    }
    SystemUiComponent(navController = navController)
}
@Composable
private fun SystemUiComponent(navController: NavHostController) {
    val mainNavControllerCurrentBackStack by navController.currentBackStackEntryAsState()
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current
    val window = context.findActivity()?.window
    if (
        mainNavControllerCurrentBackStack?.destination?.route?.contains(Screen.SignUp.route) == true
        || mainNavControllerCurrentBackStack?.destination?.route?.equals(Screen.Login.route) == true
    ) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
        WindowCompat.setDecorFitsSystemWindows(window!!, false)
    } else {
        systemUiController.setStatusBarColor(
            color = Color.Black,
            darkIcons = false
        )
        WindowCompat.setDecorFitsSystemWindows(window!!, false)
    }
}