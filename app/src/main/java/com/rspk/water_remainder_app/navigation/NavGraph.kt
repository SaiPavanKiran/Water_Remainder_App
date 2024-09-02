package com.rspk.water_remainder_app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rspk.water_remainder_app.compose.ActivityScreen
import com.rspk.water_remainder_app.compose.SplashText
import com.rspk.water_remainder_app.compose.StartScreen
import com.rspk.water_remainder_app.viewmodels.MainScreenUtilsViewModels

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    mainScreenViewModel: MainScreenUtilsViewModels = viewModel()
){
    NavHost(
        navController = navController,
        startDestination = Splash,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        composable<Splash> {
            CompositionLocalProvider(
                LocalNavController provides navController
            ) {
                SplashText()
            }
        }

        composable<Activity> {
            CompositionLocalProvider(
                LocalNavController provides navController
            ) {
                ActivityScreen(
                    mainScreenUtilsViewModels = mainScreenViewModel
                )
            }
        }

        composable<Home> {
            CompositionLocalProvider(
                LocalNavController provides navController
            ) {
                StartScreen(
                    mainScreenUtilsViewModels = mainScreenViewModel
                )
            }
        }
    }
}
