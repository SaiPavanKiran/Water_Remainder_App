package com.rspk.water_remainder_app.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.rspk.water_remainder_app.module.SendStoreData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

class TimingsStore (
    private val dataStore: DataStore<Preferences>,
){
    private val scope = CoroutineScope(Dispatchers.IO)
    companion object{
        val START_TIME = longPreferencesKey("start_time")
        val END_TIME = longPreferencesKey("end_time")
        val WATER_TIME_INTERVAL = intPreferencesKey("time_interval")
        val WATER_AMOUNT = intPreferencesKey("water_amount")
    }

    val timingsStore = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e("timingStore", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            val startTime = preferences[START_TIME] ?: 0L
            val endTime = preferences[END_TIME] ?: 0L
            val waterTimeInterval = preferences[WATER_TIME_INTERVAL] ?: 0
            val waterAmount = preferences[WATER_AMOUNT] ?: 0
            SendStoreData(
                startTime = startTime,
                endTime = endTime,
                timeInterval = waterTimeInterval,
                waterAmount = waterAmount,
            )
        }


    suspend fun changeStartTime(startTime:Long){
        scope.launch {
            dataStore.edit { preferences ->
                preferences[START_TIME] = startTime
            }
        }
    }

    suspend fun changeEndTime(endTime:Long){
        scope.launch {
            dataStore.edit { preferences ->
                preferences[END_TIME] = endTime
            }
        }
    }

    suspend fun changeTimeInterval(timeInterval:Int){
        scope.launch {
            dataStore.edit{
                it[WATER_TIME_INTERVAL] = timeInterval
            }
        }
    }

    suspend fun changeWaterAmount(waterAmount:Int){
        scope.launch {
            dataStore.edit{
                it[WATER_AMOUNT] = waterAmount
            }
        }
    }
}