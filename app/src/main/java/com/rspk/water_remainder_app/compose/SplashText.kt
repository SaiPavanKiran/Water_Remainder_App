package com.rspk.water_remainder_app.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import com.rspk.water_remainder_app.R
import com.rspk.water_remainder_app.navigation.Home
import com.rspk.water_remainder_app.navigation.LocalNavController
import com.rspk.water_remainder_app.navigation.Splash
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashText(
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val navController = LocalNavController.current
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            delay(500)
            navController.navigate(Home){
                popUpTo(Splash) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ){
        TextInput(
            text = R.string.app_name,
            fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp
        )
    }
}