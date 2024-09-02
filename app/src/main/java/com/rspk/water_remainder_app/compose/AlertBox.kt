package com.rspk.water_remainder_app.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rspk.water_remainder_app.R

@Composable
fun AlertBox(
    configuration: Configuration = LocalConfiguration.current,
    title:String = stringResource(id = R.string.permission_alert),
    text:String,
    confirmText:String = stringResource(id = R.string.grant_permission),
    cancelText:String? = stringResource(id = R.string.cancel),
    cancelClick:() -> Unit = {},
    confirmClick:() -> Unit = {},
    onDismissRequest:() -> Unit = {}
){
    val sizeModifier = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Modifier.size(
            width = configuration.screenWidthDp.dp - 75.dp,
            height = configuration.screenHeightDp.dp / 2 - 100.dp
        )
    } else {
        Modifier.size(
            width = configuration.screenWidthDp.dp / 2 - 100.dp,
            height = configuration.screenHeightDp.dp - 75.dp
        )
    }
    AlertDialog(
        modifier = sizeModifier.padding(dimensionResource(id = R.dimen.padding10)),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Box (
                contentAlignment = Alignment.BottomEnd
            ){
                Row {
                    Button(onClick = {
                        confirmClick()
                    }) {
                        Text(text = confirmText)
                    }
                    if(cancelText != null){
                        Button(onClick = {
                            cancelClick()
                        }) {
                            Text(text = cancelText)
                        }
                    }
                }
            }
        },
        title = {
            Text(
                text = title,
                fontSize = dimensionResource(id = R.dimen.font_size_30).value.sp,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size20)))
                    .background(MaterialTheme.colorScheme.surface),
                contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.padding20) , vertical = dimensionResource(id = R.dimen.padding10)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item{
                    Text(
                        text = text,
                        fontSize = dimensionResource(id = R.dimen.font_size_small2).value.sp
                    )
                }
            }
        }
    )
}