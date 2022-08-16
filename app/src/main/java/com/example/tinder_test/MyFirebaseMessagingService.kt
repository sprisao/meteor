package com.example.tinder_test

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.tinder_test"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FCM"

    /*generate the notification*/
    /* attach the notification created with the custom layout */
    /* show the notification */

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Message : ${remoteMessage.notification!!.body}")

            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)

    }

    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews(channelName,R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.app_logo,R.drawable.logo)

        return remoteView
    }

    fun generateNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId).setSmallIcon(R.drawable.logo)
                .setAutoCancel(true).setVibrate(
                    longArrayOf(1000, 1000, 1000, 1000)
                ).setOnlyAlertOnce(true).setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notificationManager.createNotificationChannel(notificationChannel)

        notificationManager.notify(0,builder.build())
        /* channel id, channel name */

    }


}