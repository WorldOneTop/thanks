package com.hallym.hlth.models

import android.content.Context
import com.hallym.hlth.R
import org.json.JSONObject

data class Term (
    var id:Int,
    var startData:String,
    var endData:String,
    var activated:Boolean?,
    var context: Context
    ){
    val activatedStr = when {
        activated==null -> {
            context.getString(R.string.apply_men_status_null)
        }
        activated!! -> {
            context.getString(R.string.apply_men_status_true)
        }
        else -> {
            context.getString(R.string.apply_men_status_false)
        }
    }
}