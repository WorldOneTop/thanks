package com.hallym.hlth.function

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class Query {
    companion object{
        const val URL:String = "https://thanks-server-ggxvc.run.goorm.io/"
    }
    private var okHttpClient:OkHttpClient = OkHttpClient()

    fun login(id:String, pw:String, onResponse: (String)-> Unit){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("asd", "Fetching FCM registration token failed", task.exception)
                onResponse("fail")
            }else{
                val request = Request.Builder()
                    .url(URL + "userLogin?userId=$id&pw=$pw&token=${task.result.toString()}")
                    .build()

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }
                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body!!.string()
                        Log.d("asd login",body)
                        onResponse(body)
                    }
                })
            }
        }
    }
    fun getDoc(date:String, onResponse: (String)-> Unit){
        val request = Request.Builder()
            .url(URL + "selectDocument?userId=${LoginStorage.id}&date=$date")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                Log.d("asd getDoc",body)
                onResponse(body)
            }
        })
    }
    fun uploadDoc(content:String, type:Int, imagePath: String?, onResponse: (JSONObject)-> Unit){
        val request = Request.Builder()
            .url(URL + "createDoc?userId=${LoginStorage.id}&docType=$type&content=$content")

        imagePath?.let {
            val file= File(imagePath)

            val body:RequestBody =
                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "image", file.name,
                        file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    )   // Upload files
                    .build()

            request.post(body)
        }

        okHttpClient.newCall(request.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                Log.d("asd query-uploadDoc",body)
                onResponse(JSONObject(body))
            }
        })
    }

    fun getAccountInfo( onResponse: (JSONObject)-> Unit){
        val request = Request.Builder()
            .url(URL + "accountInfo?userId=${LoginStorage.id}")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                Log.d("asd query-getAccountInfo",body)
                onResponse(JSONObject(body))
            }
        })
    }

    fun getTerm(activated:Boolean?, onResponse: (JSONObject) -> Unit){
        var url =URL + "selectTerm"
        activated?.let {
            url += "?activated=" + if(activated) 1 else 0
        }

        val request = Request.Builder().url(url).build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                Log.d("asd query-getTerm",body)
                onResponse(JSONObject(body))
            }
        })
    }

    fun getNoti(onResponse: (JSONObject) -> Unit){
        val request = Request.Builder()
            .url(URL + "getNotice")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                Log.d("asd getNoti",body)
                onResponse(JSONObject(body))
            }
        })
    }

    fun settingMessage(recvChat:Boolean, recvDaily:Boolean){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val request = Request.Builder()
                    .url(URL + "settingMessage?token=${task.result.toString()}&recvChat=${if(recvChat) 1 else 0}&recvDaily=${if(recvDaily) 1 else 0}")
                    .build()

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }
                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body!!.string()
                        Log.d("asd settingMessage",body)
                    }
                })
            }
        }
    }

}
