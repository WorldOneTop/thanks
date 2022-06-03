package com.hallym.hlth.function

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hallym.hlth.MainActivity
import com.hallym.hlth.R
import com.hallym.hlth.StartingActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context:Context , intent:Intent) {
        val alramIntent = Intent(context,StartingActivity::class.java)
        alramIntent.putExtra("link",arrayOf(1))
        PushMsg(context).createPushMsg(context.getString(R.string.app_name),context.getString(R.string.message_daily_body),
            PushMsg.ID_DAILY,alramIntent)
    }

}