package com.example.fcmpushnotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelID = "notification_channel"
const val channelName = "MyChannel"

class MyFirebaseInstanceIDService : FirebaseMessagingService() {
    // 1 Genrate notification
    // 2 attaching the notification with custom layout
    // 3 show notification



    // 3 show notification
    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification!=null){
            genrateNotification(message.notification!!.title!!,message.notification!!.body!!)
        }
    }

    // 1 Genrate notification
    fun genrateNotification(title: String, description: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            channelID
        )
            .setSmallIcon(R.drawable.icon_notification_foreground)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        builder = builder.setContent(getRemoteViews(title, description))
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationchannel =
                NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationchannel)
        }
        notificationManager.notify(0, builder.build())
    }

    // 2 attaching the notification with custom layout
    fun getRemoteViews(title: String, description: String): RemoteViews {
        val remoteView = RemoteViews("com.example.fcmpushnotifications", R.layout.notification)
        remoteView.setTextViewText(R.id.notification_title, title)
        remoteView.setTextViewText(R.id.notification_desc, description)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.icon_notification_foreground)
        return remoteView
    }
}