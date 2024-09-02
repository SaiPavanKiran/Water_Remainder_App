package com.rspk.water_remainder_app.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.rspk.water_remainder_app.utils.actionUpdate
import java.io.IOException

class DoneActionBroadcast:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?: return
        try {
            actionUpdate("Done",context = context)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(2)
    }
}