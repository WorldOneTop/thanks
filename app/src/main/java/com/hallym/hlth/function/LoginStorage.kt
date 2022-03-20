package com.hallym.hlth.function

import android.content.Context
import org.json.JSONObject

class LoginStorage (val context: Context){
    companion object{
        const val FILE_PATH = "login"
        var id:String? = null
            private set
        var pw:String? = null
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
    fun setIdPw(id:String, pw:String) {
        LoginStorage.id = id
        LoginStorage.pw = pw
    }
    fun saveIdPw(id:String?, pw:String?) {
        content.put("id", id)
        content.put("pw", pw)
        context.openFileOutput(FILE_PATH, Context.MODE_PRIVATE).use {
            it.write(content.toString().toByteArray())
        }
        LoginStorage.id = id
        LoginStorage.pw = pw
    }
    fun loadIdPw(){
        id = content.getString("id")
        pw = content.getString("pw")
    }
}