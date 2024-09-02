package com.rspk.water_remainder_app.compose

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.rspk.water_remainder_app.R
import com.rspk.water_remainder_app.utils.actionUpdate
import com.rspk.water_remainder_app.viewmodels.MainScreenUtilsViewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SettingsTextDisplay(
    mainScreenUtilsViewModels: MainScreenUtilsViewModels,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    //heading
    TextInput(text = R.string.app_name, fontSize = dimensionResource(id = R.dimen.font_size_mediumLarge).value.sp)
    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size15)))
    TextInput(text = R.string.description, fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp)

    //time interval
    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size35)))
    TextInput(text = R.string.time_interval, fontSize = dimensionResource(id = R.dimen.font_size_small2).value.sp, image = R.drawable.ring_svgrepo_com)
    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size5)))

    TextFieldInput(
        leadingIcon = {
            IconButton(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    if (mainScreenUtilsViewModels.editInputs)
                        mainScreenUtilsViewModels.timeToRemainder = ((mainScreenUtilsViewModels.timeToRemainder.toLongOrNull() ?: 0) + 5).toString()
                }
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.add_time))
            }
        },
        trailingIcon = {
            IconButton(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    if (mainScreenUtilsViewModels.editInputs)
                        mainScreenUtilsViewModels.timeToRemainder = ((mainScreenUtilsViewModels.timeToRemainder.toLongOrNull() ?: 0) - 5).toString()
                }
            }) {
                Icon(painter = painterResource(id = R.drawable.dash_svgrepo_com), contentDescription = stringResource(id = R.string.add_time), modifier = Modifier.size(dimensionResource(id = R.dimen.size26)))
            }
        },
        value = mainScreenUtilsViewModels.timeToRemainder,
        onValueChange = { mainScreenUtilsViewModels.timeToRemainder = it },
        enabled = mainScreenUtilsViewModels.editInputs,
    )

    //water amount
    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size20)))
    TextInput(text = R.string.water_amount, fontSize = dimensionResource(id = R.dimen.font_size_small2).value.sp, image = R.drawable.bottle_of_water_svgrepo_com)

    TextFieldInput(
        leadingIcon = { Text(text = stringResource(id = R.string.empty), fontSize = dimensionResource(id = R.dimen.font_size_26).value.sp) },
        value = mainScreenUtilsViewModels.waterAmount,
        onValueChange = {
            mainScreenUtilsViewModels.waterAmount = it
        },
        enabled = mainScreenUtilsViewModels.editInputs,
        trailingIcon = {
            Text(
                text = stringResource(id = R.string.ml),
                fontSize = dimensionResource(id = R.dimen.font_size_26).value.sp,
            )
        }
    )

    //start and end time
    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size20)))
    TextInput(text = R.string.duration, fontSize = dimensionResource(id = R.dimen.font_size_small2).value.sp, image = R.drawable.time1_svgrepo_com)

    Card(
        modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen.padding10))
    ) {
        TextFieldInput(
            leadingIcon = { Text(text = stringResource(id = R.string.start), fontSize = dimensionResource(id = R.dimen.font_size_small2).value.sp, fontWeight = FontWeight.Bold) },
            trailingIcon = { Text(text = stringResource(id = R.string.empty2), fontSize = dimensionResource(id = R.dimen.font_size_small2).value.sp, fontWeight = FontWeight.Bold) },
            value = mainScreenUtilsViewModels.startTimeTextField,
            onValueChange = { mainScreenUtilsViewModels.startTimeTextField = it },
            supportingText = {
                if (!mainScreenUtilsViewModels.timePattern.matcher(mainScreenUtilsViewModels.startTimeTextField).matches()) {
                    Text(text = stringResource(id = R.string.error_time_format))
                }
                if(mainScreenUtilsViewModels.startTimeTextField >= mainScreenUtilsViewModels.endTimeTextField){
                    Text(text = stringResource(id = R.string.start_time_error))
                }
            },
            enabled = mainScreenUtilsViewModels.editInputs,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        TextFieldInput(
            leadingIcon = { Text(text = stringResource(id = R.string.end), fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            trailingIcon = { Text(text = stringResource(id = R.string.empty2), fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            value = mainScreenUtilsViewModels.endTimeTextField,
            onValueChange = { mainScreenUtilsViewModels.endTimeTextField = it },
            supportingText = {
                if (!mainScreenUtilsViewModels.timePattern.matcher(mainScreenUtilsViewModels.endTimeTextField).matches()) {
                    Text(text = stringResource(id = R.string.error_time_format))
                }
            },
            enabled = mainScreenUtilsViewModels.editInputs,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
    }
}

@Composable
fun ActivityTextDisplay(
    list: List<String>
) {
    val focusRequester = FocusRequester()
    Card(
        modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen.padding10))
    ) {
        list.forEach {
            TextFieldInput(
                leadingIcon = { Text(text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(it.substringBefore(":").toLong()), fontSize = dimensionResource(id = R.dimen.font_size_small2).value.sp, fontWeight = FontWeight.Bold) },
                trailingIcon = {
                    Checkbox(
                        checked = it.substringAfterLast(":").trim() == stringResource(id= R.string.done),
                        onCheckedChange ={},
                    )
                },
                value = stringResource(
                    id = R.string.water_amount_in_ml,
                    it.substringAfter(":").substringBeforeLast(":")
                ),
                onValueChange = {},
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding15))
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            focusRequester.freeFocus()
                        }
                    }
            )
        }
    }
}


@Composable
fun ActivityScreenTextDisplay(
    context: Context = LocalContext.current,
    index:Int,
    time:String,
    waterAmount:String,
    endString: String
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding10))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopEnd)
        ){
            Switch(
                checked = endString == stringResource(id = R.string.done),
                onCheckedChange = {
                    val newState = if (it) "Done" else "Not Done"
                    actionUpdate(newState, selection = index+1, context)
                },
                colors = if (endString == stringResource(id = R.string.no_action)) {
                    SwitchDefaults.colors(
                        uncheckedBorderColor = MaterialTheme.colorScheme.error,
                    )
                }else{
                    SwitchDefaults.colors()
                }
            )
        }
        Text(
            text = time,
            fontSize = dimensionResource(id = R.dimen.font_size_30).value.sp,
            fontWeight = FontWeight.Bold,
            color = if(endString == stringResource(id = R.string.no_action)) MaterialTheme.colorScheme.error else Color.Unspecified
        )
        Text(
            text = if(endString == stringResource(id = R.string.no_action)) "Missing: ${waterAmount}ml" else if(endString == stringResource(id = R.string.not_done)) "Unconsumed: ${waterAmount}ml" else "WaterIntake: ${waterAmount}ml",
            fontSize = dimensionResource(id = R.dimen.font_size_small).value.sp,
            fontWeight = FontWeight.Bold,
            color = if(endString == stringResource(id = R.string.no_action)) MaterialTheme.colorScheme.error else Color.Unspecified
        )
    }
}