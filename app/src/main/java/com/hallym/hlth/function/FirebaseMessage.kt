package com.hallym.hlth.function

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hallym.hlth.models.Notice


class FirebaseMessage : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if(remoteMessage.from =="/topics/notice"){
            Notice.noticeList?.let {
                Notice.noticeList!!.add(0,Notice(remoteMessage.data["title"]!!,remoteMessage.data["content"]!!,remoteMessage.data["date"]!!,Notice.iconNotice))
            }
        }
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d("asd", "From: ${remoteMessage.from}")
//
//        // Check if message contains a data payload.
//        if (remoteMessage.data.isNotEmpty()) {
//            Log.d("asd", "Message data payload: ${remoteMessage.data}")
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
////                scheduleJob()
//            } else {
//                // Handle message within 10 seconds
////                handleNow()
//            }
//        }
//
//        // Check if message contains a notification payload.
//        remoteMessage.notification?.let {
//            Log.d("asd", "Message Notification Body: ${it.body}")
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
    }
}