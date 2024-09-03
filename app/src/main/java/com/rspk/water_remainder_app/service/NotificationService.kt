package com.rspk.water_remainder_app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.rspk.water_remainder_app.MainActivity
import com.rspk.water_remainder_app.R
import com.rspk.water_remainder_app.work.TimelyWorker.Companion.ACTIVITY_REQUEST_CODE

class NotificationService:Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            "START" -> startService()
            "STOP" -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startService(text:String = "Running background services"){
        val pendingIntent = PendingIntent.getActivity(applicationContext, ACTIVITY_REQUEST_CODE,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE)
        val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
        val notificationChannel = NotificationChannel(
            "foreground",
            "foreground_service",
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(notificationChannel)
        val notification = NotificationCompat.Builder(applicationContext,"foreground")
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setAutoCancel(false)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentIntent(pendingIntent)
            .setContentText(text)
            .setSilent(true)
            .build()

        ServiceCompat.startForeground(
            this,
            1012,
            notification,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            }else{
                0
            }
        )
    }

    override fun onBind(intent: Intent?): IBinder? = null
}