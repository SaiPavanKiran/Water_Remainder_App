package com.rspk.water_remainder_app.work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.rspk.water_remainder_app.MainActivity
import com.rspk.water_remainder_app.R

class NotificationWorker(
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

//    override suspend fun getForegroundInfo(): ForegroundInfo {
//        return ForegroundInfo(
//            NOTIFICATION_ID,
//            notification()
//        )
//    }

    override suspend fun doWork(): Result {
        notification()
        notificationManager.notify(NOTIFICATION_ID, notification())
        return Result.success()
    }

    private fun notificationChannel(){
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun notification():Notification{
        notificationChannel()
        val pendingIntent = PendingIntent.getActivity(applicationContext, ACTIVITY_REQUEST_CODE,Intent(applicationContext, MainActivity::class.java),PendingIntent.FLAG_IMMUTABLE)
        val doneActionPendingIntent = PendingIntent.getBroadcast(applicationContext, BROADCAST_REQUEST_CODE,Intent(ACTION_DONE),PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.water_glass_svgrepo_com)
            .setContentTitle("Water Remainder")
            .setContentText("It's Time to Drink Water")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.baseline_send_24,applicationContext.resources.getString(R.string.done),doneActionPendingIntent)
            .build()
    }
}