package com.brucechoe.meteor.message.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.brucechoe.meteor.MainActivity
import com.brucechoe.meteor.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

// 유저 정보 가져와서
// Firebase 서버로 메시지 보내라고 명령
// Firebase 서버에서 앱으로 메세지 보내주고
// 앱에서는 알람을 띄워줌

private const val CHANNEL_ID = "fcm_channel"

class FirebaseService : FirebaseMessagingService(){

    private val TAG = "FirebaseMessage"

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, MainActivity::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_android_black_24dp).setAutoCancel(true)
            .setContentIntent(pendingIntent).build()

        notificationManager.notify(notificationID, notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel =
            NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "My Channel description"
                enableLights(true)
                lightColor = Color.GREEN
            }
        notificationManager.createNotificationChannel(channel)

    }


}