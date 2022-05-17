package com.hallym.hlth.function

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hallym.hlth.R

class PushMsg(val context: Context) {
    companion object{
        const val ID_NOTICE = 0
        const val ID_DAILY = 1
        const val ID_REJECT = 2
        const val ID_ACCEPT = 2
    }

    fun createPushMsg(title:String?, content:String?, NOTIFICATION_ID:Int, intent: Intent){
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"

        createNotificationChannel(channelId)

        intent.setAction(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, channelId)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
    private fun createNotificationChannel(channelId:String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}