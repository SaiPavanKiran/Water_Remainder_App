package com.rspk.water_remainder_app.compose

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import com.rspk.water_remainder_app.ui.theme.inversePrimaryLight
import com.rspk.water_remainder_app.ui.theme.primaryContainerLight
import com.rspk.water_remainder_app.ui.theme.primaryLight
import com.rspk.water_remainder_app.viewmodels.MainScreenUtilsViewModels

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    mainScreenUtilsViewModels: MainScreenUtilsViewModels
) {
    val colors = listOf(primaryLight,inversePrimaryLight, primaryContainerLight)
    var batteryOptimization by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier.background(
                    brush = Brush.sweepGradient(colors = colors)
                    )
            )
        },
    ) { innerPadding ->
        if(batteryOptimization) {
            AlertBox(
                text = "Please Kindly disable Battery optimization in the settings",
                cancelClick = {
                    batteryOptimization = false
                },
                confirmClick = {
                    batteryOptimization = false
                    Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                        context.startActivity(this)
                    }
                }
            )
        }else{
            MainScreen(
                modifier = Modifier.padding(innerPadding),
                mainScreenViewModel = mainScreenUtilsViewModels,
                onBatteryOptimizationChange = {
                    batteryOptimization = it
                }
            )
        }
    }
}

