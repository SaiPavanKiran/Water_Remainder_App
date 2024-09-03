package com.rspk.water_remainder_app.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rspk.water_remainder_app.MainActivity
import com.rspk.water_remainder_app.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Locale

class TimelyWorker(
    context: Context,
    params: WorkerParameters
):CoroutineWorker(context,params) {

    companion object{
        const val NOTIFICATION_ID = 2
        const val CHANNEL_ID = "water_remainder"
        const val CHANNEL_NAME = "Water Remainder"
        const val ACTION_DONE = "com.rspk.water_remainder_app.DONE_ACTION"
        const val ACTIVITY_REQUEST_CODE = 101
        const val BROADCAST_REQUEST_CODE = 102

    }

    private val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    override suspend fun doWork(): Result {

        val startTime = inputData.getLong("startTime",0L)
        val endTime = inputData.getLong("endTime",0L)
        val waterAmount = inputData.getInt("waterAmount",0)
        val currentTime = System.currentTimeMillis()

        return if(LocalTime.now() in LocalTime.of(getTimeInPatternForHour(startTime),getTimeInPatternForMinute(startTime))..
            LocalTime.of(getTimeInPatternForHour(endTime),getTimeInPatternForMinute(endTime))){
            withContext(Dispatchers.IO){
                notification()
                externalStorage(currentTime= currentTime, waterAmount = waterAmount)
            }
            Result.success()
        }else {
            applicationContext.contentResolver.delete(
                MediaStore.Files.getContentUri("external"),
                "${MediaStore.MediaColumns.DISPLAY_NAME} LIKE ?",
                arrayOf("daily_list %")
            )
            Result.success()
        }
    }

    private fun externalStorage(currentTime:Long,waterAmount:Int){
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
                outputStream.write("$currentTime:$waterAmount:No Action\n".toByteArray())
            }
        }

    }



    private fun notificationChannel(){
        val soundUri: Uri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(applicationContext.resources.getResourcePackageName(R.raw.notify))
            .appendPath(applicationContext.resources.getResourceTypeName(R.raw.notify))
            .appendPath(applicationContext.resources.getResourceEntryName(R.raw.notify))
            .build()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)  // Use for notifications
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(soundUri,audioAttributes)
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun notification(){
        notificationChannel()

        val pendingIntent = PendingIntent.getActivity(applicationContext, ACTIVITY_REQUEST_CODE,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE)

        val doneActionPendingIntent = PendingIntent.getBroadcast(applicationContext, BROADCAST_REQUEST_CODE,
            Intent(ACTION_DONE),
            PendingIntent.FLAG_IMMUTABLE)


        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.water_glass_svgrepo_com)
            .setContentTitle("Water Remainder")
            .setContentText("It's Time to Drink Water")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.baseline_send_24,applicationContext.resources.getString(R.string.done),doneActionPendingIntent)
            .build()

        notification.color = applicationContext.resources.getColor(R.color.notification_color,applicationContext.theme)
        notificationManager.notify(NOTIFICATION_ID,notification)
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