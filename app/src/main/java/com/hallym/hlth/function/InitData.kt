package com.hallym.hlth.function

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.hallym.hlth.MainActivity
import com.hallym.hlth.models.Document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

// 로그인 성공 이후에만
class InitData(val context: Activity,var listener:() -> Unit) {
    var finished = 0
    init{
        setTodayDocumentData()
        setChatData()
    }
    private fun setTodayDocumentData(){
        Document.clearTodayData()
        Document.homeDataType = Document.todayDataType

        Query().getDoc(LoginStorage.id.toString(),Query.now()){
            try {
                val documents = JSONObject(it).getJSONArray("data")

                for(i in 0 until documents.length()){
                    Document.todayDataType[documents.getJSONObject(i).getInt("docType")]?.add(
                        Document(
                            documents.getJSONObject(i)
                        )
                    )
                }
                onFinished()
            }catch (e: JSONException){
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(context,"Can't connect to server.", Toast.LENGTH_LONG).show()
                    context.finish()
                }
            }
        }
    }

    private fun setChatData(){
//        Query().getLastChatList{
//            try {
//                ChatStorage(context).setLastChatList(it.getJSONArray("data"))
                onFinished()
//            }catch (e: JSONException){
//                CoroutineScope(Dispatchers.Main).launch{
//                    Toast.makeText(context,"Can't connect to server.", Toast.LENGTH_LONG).show()
//                    context.finish()
//                }
//            }
//        }
    }

    private fun onFinished(){
        if(++finished >= 2)
            listener()
    }
}