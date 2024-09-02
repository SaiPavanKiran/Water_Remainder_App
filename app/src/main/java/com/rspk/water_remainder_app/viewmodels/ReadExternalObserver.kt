package com.rspk.water_remainder_app.viewmodels

import android.content.ContentUris
import android.content.Context
import android.database.ContentObserver
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.rspk.water_remainder_app.module.ContentObserverPacket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException

class ReadExternalObserver(
    private val context: Context
) :MutableLiveData<ContentObserverPacket>() {

    private lateinit var observer:ContentObserver
    override fun onActive() {
        super.onActive()
        CoroutineScope(Dispatchers.IO).launch {
            postValue(readExternalStorage(context))
        }
        observer = object :ContentObserver(null){
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                CoroutineScope(Dispatchers.IO).launch {
                    postValue(readExternalStorage(context))
                }
            }
        }
        context.contentResolver.registerContentObserver(
            MediaStore.Files.getContentUri("external"),
            true,
            observer
        )
    }

    override fun onInactive() {
        super.onInactive()
        context.contentResolver.unregisterContentObserver(observer)
    }

    private suspend fun readExternalStorage(context: Context):ContentObserverPacket{
        return withContext(Dispatchers.IO){
            val list = mutableStateListOf<String>()
            var sum by mutableIntStateOf(0)
            var totalSum by mutableIntStateOf(0)

            try {
                context.contentResolver.query(
                    MediaStore.Files.getContentUri("external"),
                    arrayOf(
                        MediaStore.MediaColumns._ID,
                        MediaStore.MediaColumns.DISPLAY_NAME
                    ),
                    "${MediaStore.MediaColumns.DISPLAY_NAME} LIKE ?",
                    arrayOf("daily_list %"),
                    null
                )?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                        val id = cursor.getLong(columnIndex)
                        val uri =
                            ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), id)
                        try {
                            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                                inputStream.bufferedReader().useLines { lines ->
                                    lines.forEach {
                                        list.add(it)
                                        totalSum += it.substringAfter(":").substringBeforeLast(":").toIntOrNull() ?: 0
                                        if (it.contains("Done")) {
                                            sum += it.substringAfter(":").substringBeforeLast(":")
                                                .toIntOrNull() ?: 0
                                        }
                                        if (it.contains("Not")) {
                                            sum -= it.substringAfter(":").substringBeforeLast(":")
                                                .toIntOrNull() ?: 0
                                        }
                                    }
                                }
                            }
                        } catch (e: FileNotFoundException) {
                            Log.e("ReadExternalObserver", "File not found: $uri", e)
                        }
                    }
                }
            }catch (e: SecurityException) {
                // Handle security exceptions
                Log.e("ReadExternalObserver", "SecurityException: ${e.message}")
            }
            ContentObserverPacket(
                list = list,
                sum = sum,
                totalSum = totalSum
            )
        }
    }
}