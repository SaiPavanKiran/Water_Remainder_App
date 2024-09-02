package com.rspk.water_remainder_app.compose

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rspk.water_remainder_app.viewmodels.MainScreenUtilsViewModels

@Composable
fun WaterSettings(
    modifier: Modifier = Modifier,
    mainScreenViewModel: MainScreenUtilsViewModels,
    configuration: Configuration = LocalConfiguration.current,
    onBatteryOptimizationChange: (Boolean) -> Unit
) {
    CardForm(
        modifier = Modifier
            .width(configuration.screenWidthDp.dp - 75.dp)
    ) {
        SettingsCardItems(
            mainScreenUtilsViewModels = mainScreenViewModel,
            onBatteryOptimizationChange = onBatteryOptimizationChange
        )
    }
}

@Composable
fun WaterActivity(
    mainScreenViewModel: MainScreenUtilsViewModels,
    configuration: Configuration = LocalConfiguration.current,
    context: Context = LocalContext.current,
){
    val contentObserver = mainScreenViewModel.readExternal(context).observeAsState().value
    CardForm(
        modifier = Modifier
            .width(configuration.screenWidthDp.dp - 75.dp)
            .height((configuration.screenHeightDp.dp / 3) + 10.dp)
    ){
        ActivityCardItems(
            list = contentObserver?.list?.takeLast(2)?.reversed() ?: listOf(),
        )
    }
}