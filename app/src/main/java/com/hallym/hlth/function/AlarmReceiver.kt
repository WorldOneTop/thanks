package com.hallym.hlth.function

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hallym.hlth.MainActivity
import com.hallym.hlth.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context:Context , intent:Intent) {
        PushMsg(context).createPushMsg(context.getString(R.string.app_name),context.getString(R.string.message_notice_body),
            PushMsg.ID_DAILY,Intent(context,MainActivity::class.java))
    }

}