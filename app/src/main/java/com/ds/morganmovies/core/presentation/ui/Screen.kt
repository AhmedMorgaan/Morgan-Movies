package com.ds.morganmovies.core.presentation.ui

sealed class Screen (val route:String) {
    object Splash : Screen("splash")
    object OnBoarding : Screen("on_boarding")
    object Login : Screen("login")
}