package com.rspk.water_remainder_app.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rspk.water_remainder_app.datastore.TimingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class MainScreenUtilsViewModels @Inject constructor(
    private val timingsStore: TimingsStore
):ViewModel() {
    val timePattern: Pattern = Pattern.compile("^(?:[01]\\d|2[0-3]):[0-5]\\d$")
    var startTimeTextField by mutableStateOf("05:00")
    var startTime by mutableLongStateOf(getTimeInMillis(hour = 5, minute = 0))
    var endTime by mutableLongStateOf(getTimeInMillis(hour = 22, minute = 59))
    var endTimeTextField by mutableStateOf("22:59")
    var timeToRemainder by mutableStateOf("15")
    var waterAmount by mutableStateOf("250")
    var editInputs by mutableStateOf(false)

    init {
        // Load initial values from DataStore
        viewModelScope.launch {
            timingsStore.timingsStore.collect { timings ->
                if(timings.startTime != 0L && timings.endTime != 0L && timings.timeInterval != 0 && timings.waterAmount != 0){
                    startTime = timings.startTime
                    endTime = timings.endTime
                    startTimeTextField = getTimeInPattern(startTime)
                    endTimeTextField = getTimeInPattern(endTime)
                    timeToRemainder = timings.timeInterval.toString()
                    waterAmount = timings.waterAmount.toString()
                }
            }
        }
    }


    fun onClickCheck(context: Context){
        if (startTimeTextField.isEmpty() || !timePattern.matcher(startTimeTextField).matches()
            || (timePattern.matcher(startTimeTextField).matches() && startTimeTextField < "05:00")) {
            startTime = getTimeInMillis(hour = 5, minute = 0)
            startTimeTextField = "05:00"
            viewModelScope.launch {
                timingsStore.changeStartTime(startTime)
            }
        } else {
            startTime = getTimeInMillis(
                hour = startTimeTextField.substringBefore(":").toInt(),
                minute = startTimeTextField.substringAfter(":").toInt()
            )
            viewModelScope.launch {
                timingsStore.changeStartTime(startTime)
            }
        }
        if (endTimeTextField.isEmpty() || !timePattern.matcher(endTimeTextField).matches()
            || (timePattern.matcher(endTimeTextField).matches() && endTimeTextField > "22:59")) {
            endTime = getTimeInMillis(hour = 22, minute = 59)
            endTimeTextField = "22:59"
            viewModelScope.launch {
                timingsStore.changeEndTime(endTime)
            }
        }else{
            endTime = getTimeInMillis(
                hour = endTimeTextField.substringBefore(":").toInt(),
                minute = endTimeTextField.substringAfter(":").toInt()
            )
            viewModelScope.launch {
                timingsStore.changeEndTime(endTime)
            }
        }
        if (timeToRemainder.isEmpty() || timeToRemainder.toLong() < 15) {
            timeToRemainder = "15"
            viewModelScope.launch {
                timingsStore.changeTimeInterval(timeToRemainder.toInt())
            }
        }else if(timeToRemainder.toLong() > 300){
            timeToRemainder = "300"
            viewModelScope.launch {
                timingsStore.changeTimeInterval(timeToRemainder.toInt())
            }
        } else {
            viewModelScope.launch {
                timingsStore.changeTimeInterval(timeToRemainder.toInt())
            }
        }
        if(waterAmount.isEmpty() || waterAmount.toInt() < 250 ){
            waterAmount = "250"
            viewModelScope.launch {
                timingsStore.changeWaterAmount(waterAmount.toInt())
            }
        }else if(waterAmount.toInt() > 1500){
            waterAmount = "1500"
            viewModelScope.launch {
                timingsStore.changeWaterAmount(waterAmount.toInt())
            }
        }else{
            viewModelScope.launch {
                timingsStore.changeWaterAmount(waterAmount.toInt())
            }
        }
        Toast.makeText(context, "Checking Input Requirements", Toast.LENGTH_SHORT).show()
    }



    private fun getTimeInMillis(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    fun getTimeInPattern(time:Long):String{
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(time)
    }


    fun calculateInitialDelay(startTime: Long, intervalMinutes: Long): Long {
        val currentTime = System.currentTimeMillis()
        if (startTime > currentTime) {
            return startTime - currentTime
        }

        if(intervalMinutes != 0L) {
            val intervalMillis = TimeUnit.MINUTES.toMillis(intervalMinutes)
            val timePassedSinceStart = currentTime - startTime
            val elapsedIntervals = timePassedSinceStart / intervalMillis
            val nextScheduledTime = startTime + ((elapsedIntervals + 1) * intervalMillis)
            return nextScheduledTime - currentTime
        }else{
            return 0L
        }
    }

    fun readExternal(context: Context) =
        ReadExternalObserver(context = context)

}