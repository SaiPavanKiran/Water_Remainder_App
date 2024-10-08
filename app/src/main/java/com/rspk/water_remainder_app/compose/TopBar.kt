package com.rspk.water_remainder_app.compose

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    @StringRes title:Int? = null,
    topBarColors:TopAppBarColors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color.Transparent)
){
    TopAppBar(
        title = {
            if (title != null){
                Text(text = stringResource(id = title))
            }
        },
        colors = topBarColors,
        modifier = modifier
    )
}