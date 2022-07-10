package com.hallym.hlth.function

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Query {
    companion object{
        const val URL:String = "https://thanks.hallym.ac.kr/"
        lateinit var CSRF:String
        fun now():String{
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().timeInMillis)
        }
    }
    private var okHttpClient:OkHttpClient = OkHttpClient()

    fun checkVersion(onResponse: (Int)-> Unit){
        val request = Request.Builder()
            .url(URL + "checkVersion?isAndroid=1")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                Log.d("checkVersion",body)
                onResponse(JSONObject(body).getInt("version"))
            }
        })
    }
    fun login(id:String, pw:String, onResponse: (String)-> Unit){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("asd", "Fetching FCM registration token failed", task.exception)
                onResponse("fail")
            }else{
                val request = Request.Builder()
                    .url(URL + "userLogin?userId=$id&pw=$pw&token=${task.result}")
                    .build()

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("asd fail", e.toString())
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
    fun getDoc(id:String, date:String, onResponse: (String)-> Unit){
        val request = Request.Builder()
            .url(URL + "selectDocument?userId=$id&date=$date")
            .build()
        Log.d("asd getDoc URL",URL + "selectDocument?userId=$id&date=$date")

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
            .url(URL + "createDoc/?userId=${LoginStorage.id}&docType=$type&CSRF=$CSRF")
        Log.d("asd query-uploadDoc URL",URL + "createDoc/?userId=${LoginStorage.id}&docType=$type&CSRF=$CSRF")


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
        request.post(FormBody.Builder().add("content",content).build())

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
            .url(URL + "accountInfo?userId=${LoginStorage.id}&CSRF=$CSRF")
            .build()
        Log.d("asd query-getAccountInfo URL",URL + "accountInfo?userId=${LoginStorage.id}&CSRF=$CSRF")

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
        Log.d("asd query-getTerm URL",url)

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
        Log.d("asd getNoti URL",URL + "getNotice")

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

    fun getMenteesDoc(onResponse: (JSONObject) -> Unit){
        val date = now()
        val request = Request.Builder()
            .url(URL + "getMenteesDoc?userId=${LoginStorage.id}&date=$date&CSRF=$CSRF")
            .build()
        Log.d("asd getMenteesDoc URL",URL + "getMenteesDoc?userId=${LoginStorage.id}&date=$date&CSRF=$CSRF")
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                Log.d("asd getMenteesDoc",body)
                onResponse(JSONObject(body))
            }
        })
    }

    fun sendChat(receiverId:Int, content:String, onResponse: (JSONObject) -> Unit){
        val request = Request.Builder()
            .url(URL + "sendChat/?senderId=${LoginStorage.id}&receiverId=$receiverId&CSRF=$CSRF")
        Log.d("asd sendChat URL",URL + "sendChat/?senderId=${LoginStorage.id}&receiverId=$receiverId&CSRF=$CSRF")

        request.post(FormBody.Builder().add("content",content).build())

        okHttpClient.newCall(request.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                Log.d("asd sendChat",body)
                onResponse(JSONObject(body))
            }
        })
    }
    fun readChat(receiverId:Int){
        val request = Request.Builder()
            .url(URL + "readChat?senderId=${LoginStorage.id}&receiverId=$receiverId&CSRF=$CSRF")
            .build()
        Log.d("asd readChat URL", URL + "readChat?senderId=${LoginStorage.id}&receiverId=$receiverId&CSRF=$CSRF")

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                Log.d("asd readChat",response.body!!.string())
            }
        })
    }

    fun applyMentoring(term:Int,applyType:Int, onResponse: (JSONObject) -> Unit){
        val request = Request.Builder()
            .url(URL + "createSignup/?userId=${LoginStorage.id}&term=$term&userType=$applyType&CSRF=$CSRF")
            .build()
        Log.d("asd applyMentoring URL", URL + "createSignup/?userId=${LoginStorage.id}&term=$term&userType=$applyType&CSRF=$CSRF")

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                Log.d("asd applyMentoring",body)
                onResponse(JSONObject(body))
            }
        })
    }
    fun getChatUserList( onResponse: (JSONObject) -> Unit){
        val request = Request.Builder()
            .url(URL + "getChatUserList/?userId=${LoginStorage.id}&CSRF=$CSRF")
            .build()
        Log.d("asd getChatUserList URL", URL + "getChatUserList/?userId=${LoginStorage.id}&CSRF=$CSRF")

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                Log.d("asd getChatUserList",body)
                onResponse(JSONObject(body))
            }
        })
    }

}
