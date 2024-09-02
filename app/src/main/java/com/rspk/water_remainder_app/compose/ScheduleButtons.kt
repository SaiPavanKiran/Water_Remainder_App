package com.rspk.water_remainder_app.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.rspk.water_remainder_app.R
import com.rspk.water_remainder_app.viewmodels.MainScreenUtilsViewModels
import com.rspk.water_remainder_app.work.TimelyWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val WORKER_UNIQUE_NAME = "timelyWork"
private const val WORKER_TAG = "time_check"

@Composable
fun ScheduleButtons(
    startTime: Long,
    endTime: Long,
    timeToRemainder: String,
    startTimeTextField: String,
    endTimeTextField: String,
    editInputs: Boolean,
    onClick: () -> Unit,
    onClickChanged: (Boolean) -> Unit,
    batteryOptimizationChange: (Boolean) -> Unit,
    mainScreenViewModel: MainScreenUtilsViewModels,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)
    val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()
    val workRequest = PeriodicWorkRequestBuilder<TimelyWorker>(timeToRemainder.toLongOrNull() ?: 15, TimeUnit.MINUTES,
        timeToRemainder.toLongOrNull() ?: 15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .setInitialDelay(mainScreenViewModel.calculateInitialDelay(startTime, timeToRemainder.toLongOrNull() ?: 15L), TimeUnit.MILLISECONDS)
        .addTag(WORKER_TAG)
        .setInputData(
            workDataOf(
                "startTime" to startTime,
                "endTime" to endTime,
                "waterAmount" to (mainScreenViewModel.waterAmount.toIntOrNull() ?: 0),
            )
        )
        .build()
    val workerInfo by workManager.getWorkInfosByTagLiveData(WORKER_TAG).observeAsState()

    Row {
        Button(onClick = {
            coroutineScope.launch {
                if (!editInputs) {
                    onClickChanged(true)
                    workManager.cancelUniqueWork(WORKER_UNIQUE_NAME)
                } else {
                    onClick()
                    onClickChanged(false)
                }
            }
        }) {
            Text(text = if (editInputs) stringResource(id = R.string.stop_editing_inputs) else stringResource(id = R.string.edit_inputs))
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size5)))
        Button(
            onClick = {
                if (workerInfo?.isEmpty() == true) {
                    batteryOptimizationChange(true)
                }
                coroutineScope.launch(Dispatchers.Default) {
                    if ((workerInfo?.any { it.state.isFinished } == true || workerInfo?.isEmpty() == true)
                        && mainScreenViewModel.getTimeInPattern(startTime) == startTimeTextField && mainScreenViewModel.getTimeInPattern(endTime) == endTimeTextField) {

                        workManager.enqueueUniquePeriodicWork(WORKER_UNIQUE_NAME, ExistingPeriodicWorkPolicy.UPDATE, workRequest)
                    } else {
                        workManager.cancelUniqueWork(WORKER_UNIQUE_NAME)
                    }
                }
            },
            enabled = !editInputs
        ) {
            Text(text = if (workerInfo?.any { it.state.isFinished } == true || workerInfo?.isEmpty() == true) stringResource(id = R.string.start_notify_me) else stringResource(id = R.string.stop_notify_me))
        }
    }
}