package com.rspk.water_remainder_app.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.rspk.water_remainder_app.R
import com.rspk.water_remainder_app.navigation.Home
import com.rspk.water_remainder_app.navigation.LocalNavController

@Composable
fun ActivityHead(
    modifier: Modifier = Modifier,
    userInfo:(Boolean) -> Unit
) {
    val navController = LocalNavController.current
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(dimensionResource(id = R.dimen.padding20))
    ){
        IconButton(onClick = {
            navController.navigate(Home){
                popUpTo(Home) { inclusive = true }
            }
        }) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                contentDescription = "Back",
                modifier = Modifier.size(dimensionResource(id = R.dimen.size30)),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            TextInput(
                text = R.string.your_activity,
                fontSize = dimensionResource(id = R.dimen.font_size_large).value.sp,
            )
        }

        IconButton(
            onClick = {
                userInfo(true)
            },
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_info_outline_24),
                contentDescription = "Small Info",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size30)),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
            )
        }

    }
}