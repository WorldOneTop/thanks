package com.hallym.hlth.function

import android.content.Context
import com.hallym.hlth.R
import org.json.JSONObject

class LoginStorage (val context: Context){
    companion object{
        const val FILE_PATH = "login"
        var id:String? = null
            private set
        var pw:String? = null
            private set
        var status:Int? = null
            private set
    }
    var content:JSONObject

    init {
        try{
            content = JSONObject(context.openFileInput(FILE_PATH).bufferedReader().readLine())
        }catch (e: Exception){ // file not fond or json except
            context.openFileOutput(FILE_PATH, Context.MODE_PRIVATE).use {
                val data = mapOf("id" to null,"pw" to null)
                it.write(data.toString().toByteArray())
                content = JSONObject(data)
            }
        }
    }
    fun setData(id:String, pw:String,status:Int) {
        LoginStorage.id = id
        LoginStorage.pw = pw
        LoginStorage.status = status
    }
    fun saveData(id:String?, pw:String?,status:Int) {
        content.put("id", id)
        content.put("pw", pw)
        content.put("status", status)
        context.openFileOutput(FILE_PATH, Context.MODE_PRIVATE).use {
            it.write(content.toString().toByteArray())
        }
        LoginStorage.id = id
        LoginStorage.pw = pw
        LoginStorage.status = status
    }
    fun loadData(){
        id = content.getString("id")
        pw = content.getString("pw")
        status = content.getInt("status")
    }
    fun statusToStr():String{
        return when(status){
            1 -> context.getString(R.string.status_1)
            2 -> context.getString(R.string.status_2)
            3 -> context.getString(R.string.status_3)
            4 -> context.getString(R.string.status_4)
            5 -> context.getString(R.string.status_5)
            else -> "??"
        }
    }
}