package com.rspk.water_remainder_app.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun ActivityBody(
    modifier: Modifier = Modifier,
    configuration: Configuration = LocalConfiguration.current,
    list: List<String>,
    sum: Int,
    totalSum: Int
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        ActivityListGrids(
            list = list,
            modifier = Modifier
                .weight(1f)
        )
        when(configuration.orientation){
            Configuration.ORIENTATION_PORTRAIT -> {
                ProgressBarPortrait(
                    sum = sum,
                    totalSum = totalSum,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                ProgressBarLandscape(
                    sum = sum,
                    totalSum = totalSum,
                    modifier = Modifier
                        .weight(1f)
                )
            }

            else -> {}
        }
    }
}