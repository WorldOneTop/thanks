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
import com.hallym.hlth.models.Notice


class FirebaseMessage : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        when(remoteMessage.data["category"]) {
             "notice" -> {
                Notice.noticeList?.let {
                    Notice.noticeList!!.add(0,Notice(remoteMessage.data["title"]!!,remoteMessage.data["body"]!!,remoteMessage.data["date"]!!,Notice.iconNotice))
                }

                 createPushMsg(getString(R.string.app_name),getString(R.string.message_notice_body),0,Intent(applicationContext,MainActivity::class.java))
            }
            "readChat" -> {
                if(remoteMessage.data["senderId"]!!.toInt() == ChatInActivity.userId){
                    val intent = Intent("Chatting")
                    intent.putExtra("userId", remoteMessage.data["senderId"]!!.toInt())
                    intent.putExtra("category", "read")
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                // DB 설정
                ChatController(ChatDB(applicationContext).writableDatabase).readAllChat(remoteMessage.data["senderId"]!!)

            }
            "sendChat" -> {
                val currentRoom =remoteMessage.data["senderId"]!!.toInt() == ChatInActivity.userId
                val intent = Intent("Chatting")
                intent.putExtra("userId", remoteMessage.data["senderId"]!!.toInt())
                intent.putExtra("category", "send")
                intent.putExtra("content", remoteMessage.data["content"])
                intent.putExtra("date", remoteMessage.data["date"])
                intent.putExtra("name", remoteMessage.data["senderName"])
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

                if(!currentRoom) {
                    createPushMsg(remoteMessage.data["senderName"].toString(),remoteMessage.data["content"].toString(),remoteMessage.data["senderId"]!!.toInt()
                        ,Intent(applicationContext,MainActivity::class.java))
                }
                // DB 설정
                ChatController(ChatDB(applicationContext).writableDatabase).recvChat(
                    remoteMessage.data["senderId"]!!.toInt(), remoteMessage.data["content"].toString(), remoteMessage.data["date"].toString(),remoteMessage.data["senderName"].toString(), currentRoom
                )

            }
        }

    }


    private fun createPushMsg(title:String, content:String, NOTIFICATION_ID:Int, intent:Intent ){
        val channelId = "$packageName-${getString(R.string.app_name)}"

        createNotificationChannel(channelId)

        val pendingIntent = PendingIntent.getActivity(baseContext, 0,
            intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(baseContext, channelId)
        builder.setSmallIcon(R.drawable.ic_app)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(baseContext)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
    private fun createNotificationChannel(channelId:String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}