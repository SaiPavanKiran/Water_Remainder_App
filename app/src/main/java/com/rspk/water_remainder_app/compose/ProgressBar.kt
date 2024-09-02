package com.rspk.water_remainder_app.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.rspk.water_remainder_app.R

@Composable
fun ProgressBarPortrait(
    modifier: Modifier = Modifier,
    configuration: Configuration = LocalConfiguration.current,
    sum:Int,
    totalSum:Int
){
    val progress = { sum.toFloat() / totalSum.toFloat() }
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Box{
            LinearProgressIndicator(
                modifier = Modifier
                    .wrapContentSize(unbounded = true)
                    .width(configuration.screenHeightDp.dp - 200.dp)
                    .height(dimensionResource(id = R.dimen.size75))
                    .rotate(270f)
                ,
                color = MaterialTheme.colorScheme.inversePrimary,
                trackColor = MaterialTheme.colorScheme.surfaceContainer,
                strokeCap = StrokeCap.Round,
                progress = progress
            )
            TextFieldInput(
                value = "${(sum.toFloat()/1000.0)}L.",
                onValueChange = {},
                readOnly = true
            )
        }
        ScaleLines(
            maxProgress = 120,
            step = 1,
            modifier = Modifier
                .height(configuration.screenHeightDp.dp - 200.dp),
            value = "PORTRAIT"
        )
    }
}

@Composable
fun ProgressBarLandscape(
    modifier: Modifier = Modifier,
    configuration: Configuration = LocalConfiguration.current,
    sum:Int,
    totalSum:Int
){
    val progress = { sum.toFloat() / totalSum.toFloat() }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box{
            LinearProgressIndicator(
                modifier = Modifier
                    .width(configuration.screenWidthDp.dp - 200.dp)
                    .height(dimensionResource(id = R.dimen.size75))
                ,
                color = MaterialTheme.colorScheme.inversePrimary,
                trackColor = MaterialTheme.colorScheme.surfaceContainer,
                strokeCap = StrokeCap.Round,
                progress = progress
            )
            TextFieldInput(
                value = "${(sum.toFloat()/1000.0)}L.",
                onValueChange = {},
                readOnly = true
            )
        }
        ScaleLines(
            maxProgress = 120,
            step = 1,
            modifier = Modifier
                .fillMaxWidth(),
            value = "LANDSCAPE"
        )
    }
}