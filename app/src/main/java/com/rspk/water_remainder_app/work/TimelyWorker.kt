package com.rspk.water_remainder_app.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.rspk.water_remainder_app.R
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Locale

class TimelyWorker(
    context: Context,
    params: WorkerParameters
):CoroutineWorker(context,params) {
    private val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {

        val startTime = inputData.getLong("startTime",0L)
        val endTime = inputData.getLong("endTime",0L)
        val waterAmount = inputData.getInt("waterAmount",0)
        val currentTime = System.currentTimeMillis()
        val workManager = WorkManager.getInstance(applicationContext)
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .addTag("water_remainder")
//                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
        if(LocalTime.now().hour == 23 || LocalTime.now().hour in 0..4){
            applicationContext.contentResolver.delete(
                MediaStore.Files.getContentUri("external"),
                "${MediaStore.MediaColumns.DISPLAY_NAME} LIKE ?",
                arrayOf("daily_list %")
            )
        }
        return if(LocalTime.now() in LocalTime.of(getTimeInPatternForHour(startTime),getTimeInPatternForMinute(startTime))..
            LocalTime.of(getTimeInPatternForHour(endTime),getTimeInPatternForMinute(endTime))){
            setForeground(createForeground(title = "Water Remainder", text = "running..."))
            workManager.enqueue(workRequest)
            externalStorage(currentTime= currentTime, waterAmount = waterAmount, action = "No Action")
            Result.success()
        }else {
            setForeground(createForeground(title = "Checking Criteria" , text = "Criteria not met"))
            Result.success()
        }
    }

    private fun externalStorage(currentTime:Long,waterAmount:Int,action:String){
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns._ID,currentTime)
            put(MediaStore.MediaColumns.DISPLAY_NAME,"daily_list ")
            put(MediaStore.MediaColumns.MIME_TYPE,"text/plain")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/water_remainder")
        }
        val fileUri: Uri?
        val cursor = applicationContext.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME
            ),//items to select
            "${MediaStore.MediaColumns.DISPLAY_NAME} LIKE ?",
            arrayOf("daily_list %"),
            null
        )

        fileUri = if (cursor?.moveToFirst() == true) {
            // If file exists, get its URI
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val id = cursor.getLong(idColumnIndex)
            ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), id)
        } else {
            // File does not exist, create a new one
            applicationContext.contentResolver.insert(
                MediaStore.Files.getContentUri("external"),
                contentValues
            )
        }

        cursor?.close()

        // Write data to the file (append if it exists)
        fileUri?.let { uri ->
            applicationContext.contentResolver.openOutputStream(uri, "wa")?.use { outputStream ->
                outputStream.write("$currentTime:$waterAmount:$action\n".toByteArray())
            }
        }


    }

    private fun createForeground(title:String,text:String ):ForegroundInfo{
//        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(workRequestId)
        val notificationChannel = NotificationChannel(
            "foreground",
            "foreground_service",
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(notificationChannel)
        val notification = NotificationCompat.Builder(applicationContext,"foreground")
            .setSmallIcon(R.drawable.baseline_water_drop_24)
            .setAutoCancel(false)
            .setContentTitle(title)
            .setContentText(text)
//            .addAction(R.drawable.baseline_send_24,"Stop Foreground",intent)
            .build()

        return ForegroundInfo(34,notification)
    }

    private fun getTimeInPatternForHour(time:Long):Int{
        val dateFormat = SimpleDateFormat("HH", Locale.getDefault())
        return dateFormat.format(time).toInt()
    }

    private fun getTimeInPatternForMinute(time:Long):Int{
        val dateFormat = SimpleDateFormat("mm", Locale.getDefault())
        return dateFormat.format(time).toInt()
    }
}