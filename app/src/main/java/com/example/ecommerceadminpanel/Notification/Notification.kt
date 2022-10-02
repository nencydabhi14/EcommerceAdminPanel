package com.example.ecommerceadminpanel.Notification

import android.app.Notification.DEFAULT_SOUND
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.ecommerceadminpanel.R


object Notification {

    // Notification Builder
    fun exampleNotification(context: Context, title: String, description: String) {

        var notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createExampleNotificationChannel(notificationManager)

        var builder = createExampleNotificationCompat(context, title, description)
        notificationManager.notify(1, builder.build())
    }

    // Notification chanel
    private fun createExampleNotificationChannel(notificationManager: NotificationManager) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "MyNotification", "MyNotificationChannel", NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

    //Notification Style
    private fun createExampleNotificationCompat(
        context: Context, title: String, description: String
    ): NotificationCompat.Builder {

        return NotificationCompat.Builder(context, "MyNotification").setContentTitle(title)
            .setContentText(description).setSmallIcon(R.drawable.category)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(DEFAULT_SOUND)
    }
}