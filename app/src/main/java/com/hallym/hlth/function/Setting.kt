package com.hallym.hlth.function

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.hallym.hlth.R

class Setting (val context: Context) {
    companion object{
        const val FILE_PATH = "app_setting"
        var color:Int = 0
            private set
        var isLock:Boolean = false
            private set
        var pin:String? = null
            private set
        var isFingerLock:Boolean = false
            private set
        var isAutoLogin:Boolean = false
            private set
        var firstPage:Int = 0
            private set
        var isRecvNoti:Boolean = false
            private set
        var isRecvDailyNoti:Boolean = false
            private set
        var notiCategory:String? = null
            private set
        var isRecvChat:Boolean = false
            private set

        fun getIndexToColor(index:Int):Int{
            return when(index){
                0 -> R.color.green
                1 -> R.color.orange
                2 -> R.color.blue
                3 -> R.color.purple
                4 -> R.color.red
                5 -> R.color.indigo
                else -> R.color.green
            }
        }
    }

    private val preference = context.getSharedPreferences(FILE_PATH, AppCompatActivity.MODE_PRIVATE)
    private val editPreference = preference.edit()

    fun loadData(){
        color = preference.getInt("color",0)
        isLock = preference.getBoolean("isLock",false)
        pin = preference.getString("pin",null)
        isFingerLock = preference.getBoolean("isFingerLock",false)
        isAutoLogin = preference.getBoolean("isAutoLogin",false)
        firstPage = preference.getInt("firstPage",0)
        isRecvNoti = preference.getBoolean("isRecvNoti",false)
        isRecvDailyNoti = preference.getBoolean("isRecvDailyNoti",false)
        notiCategory = preference.getString("notiCategory",null)
        isRecvChat = preference.getBoolean("isRecvChat",false)
    }
    fun apply(){
        if(isRecvNoti != preference.getBoolean("isRecvNoti",false)){
            if(isRecvNoti){
                Firebase.messaging.subscribeToTopic("notice")
            }else{
                Firebase.messaging.unsubscribeFromTopic("notice")
            }
        }
        if(isRecvDailyNoti != preference.getBoolean("isRecvDailyNoti",false)){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val pendingIntent = PendingIntent.getBroadcast(context, 1, Intent(context, AlarmReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

            if(isRecvDailyNoti){
                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 20)
                }
                alarmManager?.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }else{
                alarmManager?.cancel(pendingIntent)
            }
        }

        if(isAutoLogin){
            LoginStorage(context).saveData(LoginStorage.id,LoginStorage.pw, LoginStorage.status!!)
        }


        editPreference.apply()
    }
    fun setColor(data: Int){
        editPreference.putInt("color",data)
        color = data
    }
    fun setLock(data: Boolean){
        editPreference.putBoolean("isLock",data)
        isLock = data
    }
    fun setPin(data: String?){
        editPreference.putString("pin",data)
        pin = data
    }
    fun setFingerLock(data: Boolean){
        editPreference.putBoolean("isFingerLock",data)
        isFingerLock = data
    }
    fun setAutoLogin(data: Boolean){
        editPreference.putBoolean("isAutoLogin",data)
        isAutoLogin = data
    }
    fun setFirstPage(data: Int){
        editPreference.putInt("firstPage",data)
        firstPage = data
    }
    fun setRecvNoti(data: Boolean){
        editPreference.putBoolean("isRecvNoti",data)
        isRecvNoti = data
    }
    fun setRecvDailyNoti(data: Boolean){
        editPreference.putBoolean("isRecvDailyNoti",data)
        isRecvDailyNoti = data
    }
    fun setNotiCategory(data: String?){
        editPreference.putString("notiCategory",data)
        notiCategory = data
    }
    fun setRecvChat(data: Boolean){
        editPreference.putBoolean("isRecvChat",data)
        isRecvChat = data
    }
}