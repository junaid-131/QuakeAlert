package com.example.quakealert.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat


object NotificationHelper {
    fun send(context: Context, msg: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "quake_alert"


        if (Build.VERSION.SDK_INT >= 26) {
            manager.createNotificationChannel(
                NotificationChannel(channelId, "Alerts", NotificationManager.IMPORTANCE_HIGH)
            )
        }


        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Quake Alert")
            .setContentText(msg)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .build()


        manager.notify(1, notification)
    }
}