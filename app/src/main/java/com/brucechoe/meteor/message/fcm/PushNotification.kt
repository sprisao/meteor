package com.brucechoe.meteor.message.fcm

data class PushNotification (
    val data: NotificationData,
    val to: String
)
