package com.rspk.water_remainder_app.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rspk.water_remainder_app.R
import com.rspk.water_remainder_app.ui.theme.inversePrimaryLight
import com.rspk.water_remainder_app.ui.theme.primaryLight
import com.rspk.water_remainder_app.viewmodels.MainScreenUtilsViewModels

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onBatteryOptimizationChange: (Boolean) -> Unit,
    mainScreenViewModel: MainScreenUtilsViewModels = viewModel()
) {
    val colors = listOf( inversePrimaryLight ,primaryLight)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size35))
            .background(
                brush = Brush.horizontalGradient(colors = colors)
            )
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){
        Image(
            painter = painterResource(id = R.drawable._706847),
            contentDescription = stringResource(id = R.string.image),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary,blendMode = BlendMode.Modulate)
        )
    }

    LazyColumn (
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(top = dimensionResource(id = R.dimen.padding20) , bottom = dimensionResource(id = R.dimen.padding400))
    ) {

        item {
            WaterSettings(
                mainScreenViewModel = mainScreenViewModel,
                onBatteryOptimizationChange = onBatteryOptimizationChange
            )

        }

        item {
            Spacer(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size40))
            )
        }

        item {
            TextInput(
                text = R.string.your_activity,
                fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
                brush = Brush.linearGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary,MaterialTheme.colorScheme.primaryContainer)
                )
            )
        }

        item {
            Spacer(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size10))
            )
        }

        item {
            WaterActivity(
                mainScreenViewModel = mainScreenViewModel
            )
        }
    }
}
