package com.rspk.water_remainder_app.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.rspk.water_remainder_app.R
import com.rspk.water_remainder_app.viewmodels.MainScreenUtilsViewModels

@Composable
fun ActivityScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    mainScreenUtilsViewModels: MainScreenUtilsViewModels
){
    var userInfo by rememberSaveable { mutableStateOf(false) }
    val contentObserver = mainScreenUtilsViewModels.readExternal(context).observeAsState().value
    if(!userInfo){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(dimensionResource(id = R.dimen.padding10))
        ) {
            ActivityHead(
                userInfo = {
                    userInfo = it
                }
            )
            ActivityBody(
                list = contentObserver?.list?.reversed() ?: listOf(),
                sum = contentObserver?.sum ?: 0,
                totalSum = contentObserver?.totalSum ?: 0
            )
        }
    }else{
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ){
            AlertBox(
                title = stringResource(id = R.string.user_info_alert),
                text = stringResource(id = R.string.user_dialog),
                cancelText = null,
                confirmText = stringResource(id = R.string.ok_confirm),
                confirmClick = {
                    userInfo = false
                },
                onDismissRequest = {
                    userInfo = false
                }
            )
        }
    }

}