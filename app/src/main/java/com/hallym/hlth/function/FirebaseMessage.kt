package com.hallym.hlth.function

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hallym.hlth.ChatInActivity
import com.hallym.hlth.MainActivity
import com.hallym.hlth.R
import com.hallym.hlth.StartingActivity
import com.hallym.hlth.models.Notice


class FirebaseMessage : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val intent = Intent(applicationContext,StartingActivity::class.java)

        when(remoteMessage.data["category"]) {
             "notice" -> {
                Notice.noticeList?.let {
                    Notice.noticeList!!.add(0,Notice(remoteMessage.data["id"]!!.toInt(),remoteMessage.data["title"]!!,remoteMessage.data["body"]!!,remoteMessage.data["date"]!!,Notice.iconNotice))
                }
                 intent.putExtra("link",intArrayOf(0,1,remoteMessage.data["id"]!!.toInt()))
                 PushMsg(applicationContext).createPushMsg(getString(R.string.app_name),getString(R.string.message_notice_body),PushMsg.ID_NOTICE,intent)
            }
            "readChat" -> {
                if(remoteMessage.data["senderId"]!!.toInt() == ChatInActivity.userId){
                    val pendingIntent = Intent("Chatting")
                    pendingIntent.putExtra("userId", remoteMessage.data["senderId"]!!.toInt())
                    pendingIntent.putExtra("category", "read")
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(pendingIntent)
                }
                // DB 설정
                ChatController(ChatDB(applicationContext).writableDatabase).readAllChat(remoteMessage.data["senderId"]!!)
            }
            "sendChat" -> {
                val currentRoom =remoteMessage.data["senderId"]!!.toInt() == ChatInActivity.userId
                val pendingIntent = Intent("Chatting")
                pendingIntent.putExtra("userId", remoteMessage.data["senderId"]!!.toInt())
                pendingIntent.putExtra("category", "send")
                pendingIntent.putExtra("content", remoteMessage.data["content"])
                pendingIntent.putExtra("date", remoteMessage.data["date"])
                pendingIntent.putExtra("name", remoteMessage.data["senderName"])
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(pendingIntent)

                if(!currentRoom && Setting(applicationContext).getRecvChat()) {
                    intent.putExtra("link",intArrayOf(2,remoteMessage.data["senderId"]!!.toInt()))
                    PushMsg(applicationContext).createPushMsg(remoteMessage.data["senderName"].toString(),remoteMessage.data["content"].toString(),remoteMessage.data["senderId"]!!.toInt()
                        ,intent)
                }
                // DB 설정
                ChatController(ChatDB(applicationContext).writableDatabase).recvChat(
                    remoteMessage.data["senderId"]!!.toInt(), remoteMessage.data["content"].toString(), remoteMessage.data["date"].toString(),remoteMessage.data["senderName"].toString(), currentRoom
                )
            }
            "mentoringReject" -> {
                LoginStorage.status?.let { LoginStorage.status = 1}
                PushMsg(applicationContext).createPushMsg(remoteMessage.data["title"].toString(),remoteMessage.data["content"].toString(),PushMsg.ID_REJECT,intent)
            }
            "mentoringAccept" -> {
                LoginStorage.status?.let { LoginStorage.status = remoteMessage.data["userStatus"]!!.toInt()}
                PushMsg(applicationContext).createPushMsg(remoteMessage.data["title"].toString(),null,PushMsg.ID_ACCEPT,intent)
            }
        }

    }


}