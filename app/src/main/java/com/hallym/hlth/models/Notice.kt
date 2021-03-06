package com.hallym.hlth.models

import com.hallym.hlth.R

data class Notice(
    var id:Int,
    var title:String,
    var content:String,
    var date:String,
    var icon:Int
) {
    companion object{
        var noticeList:ArrayList<Notice>? = null
        const val iconNotice = R.drawable.ic_round_notifications_24
    }
}