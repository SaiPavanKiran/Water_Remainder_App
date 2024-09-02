package com.rspk.water_remainder_app.compose

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import com.rspk.water_remainder_app.R
import com.rspk.water_remainder_app.navigation.Activity
import com.rspk.water_remainder_app.navigation.LocalNavController
import com.rspk.water_remainder_app.viewmodels.MainScreenUtilsViewModels
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SettingsCardItems(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    mainScreenUtilsViewModels: MainScreenUtilsViewModels,
    onBatteryOptimizationChange: (Boolean) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = dimensionResource(id = R.dimen.padding35),
                horizontal = dimensionResource(id = R.dimen.padding20)
            )
    ){
        SettingsTextDisplay(mainScreenUtilsViewModels = mainScreenUtilsViewModels)
        ScheduleButtons(
            timeToRemainder = mainScreenUtilsViewModels.timeToRemainder,
            startTime = mainScreenUtilsViewModels.startTime,
            endTime = mainScreenUtilsViewModels.endTime,
            startTimeTextField = mainScreenUtilsViewModels.startTimeTextField,
            endTimeTextField = mainScreenUtilsViewModels.endTimeTextField,
            onClick = { mainScreenUtilsViewModels.onClickCheck(context) },
            editInputs = mainScreenUtilsViewModels.editInputs,
            onClickChanged = { mainScreenUtilsViewModels.editInputs = it },
            mainScreenViewModel = mainScreenUtilsViewModels,
            batteryOptimizationChange = { onBatteryOptimizationChange(it) }
        )

    }
}

@Composable
fun ActivityCardItems(
    modifier: Modifier = Modifier,
    list: List<String>
){
    val navController = LocalNavController.current
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = dimensionResource(id = R.dimen.padding35),
                horizontal = dimensionResource(id = R.dimen.padding20)
            )
    ) {
        item {
            TextInput(
                text = R.string.current_date,
                textInput = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            )
        }
        item {
            ActivityTextDisplay(list = list)
        }
        item {
            TextButton(onClick = {
                navController.navigate(Activity)
            }) {
                TextInput(
                    text = R.string.show_more,
                    fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp
                )
            }
        }
    }
}