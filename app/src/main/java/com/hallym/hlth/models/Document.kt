package com.hallym.hlth.models

import android.content.Context
import com.hallym.hlth.R
import org.json.JSONObject
import java.util.ArrayList

data class Document(
    var docId: Int,
    var content: String,
    var docType: Int,
    var registerDate: String,
    var fileUrl: String?
) {
    companion object{
        var todayDataType:Map<Int,ArrayList<Document>> = mapOf(0 to ArrayList<Document>(), 1 to ArrayList<Document>(), 2 to ArrayList<Document>(), 3 to ArrayList<Document>())
        var homeDataType:Map<Int,ArrayList<Document>> = todayDataType

        fun clearTodayData() {
            todayDataType = mapOf(0 to ArrayList<Document>(), 1 to ArrayList<Document>(), 2 to ArrayList<Document>(), 3 to ArrayList<Document>())
        }
        fun clearHomeData() {
            homeDataType = mapOf(0 to ArrayList<Document>(), 1 to ArrayList<Document>(), 2 to ArrayList<Document>(), 3 to ArrayList<Document>())
        }
    }
    constructor(data:JSONObject): this(
            data.getInt("docId"),
            data.getString("content"),
            data.getInt("docType"),
            data.getString("docId"),
            data.getString("fileUrl").ifEmpty { null }
    )

    fun typeToStr(context: Context):String{

        return when(docType){
            0 -> context.getString(R.string.title_thanks_short)
            1 -> context.getString(R.string.title_save_short)
            2 -> context.getString(R.string.title_kind_short)
            3 -> context.getString(R.string.title_book_short)
            else -> "??"
        }
    }
}