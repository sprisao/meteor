package com.example.tinder_test.message.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

// 유저 정보 가져와서
// Firebase 서버로 메시지 보내라고 명령
// Firebase 서버에서 앱으로 메세지 보내주고
// 앱에서는 알람을 띄워줌

class FirebaseService : FirebaseMessagingService(){

    private val TAG = "FirebaseMessage"

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.e(TAG, message.notification?.title.toString())
        Log.e(TAG, message.notification?.body.toString())

    }
}