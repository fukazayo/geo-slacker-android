package com.fukazayo.geoslacker.datasource.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fukazayo.geoslacker.R
import com.fukazayo.geoslacker.common.Constants

class NotificationClient {
    companion object {
        fun sendNotification(context: Context, title: String, message: String) {
            val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
                .build()

            val manager = NotificationManagerCompat.from(context)
            manager.notify(Math.random().toInt(), notification)
        }
    }
}
