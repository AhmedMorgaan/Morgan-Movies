package com.ds.morganmovies.core.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ds.morganmovies.core.presentation.ui.Screen
import com.ds.morganmovies.feature_auth.presentation.login.LoginScreen
import com.ds.morganmovies.feature_auth.presentation.onboarding.OnBoardingScreen
import com.ds.morganmovies.feature_auth.presentation.splash.SplashScreen

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
    }
}