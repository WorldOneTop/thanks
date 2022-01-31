package com.hallym.hlth.models


class Chatting(
    var chatRoom: Int,
    var userId: Int,
    var userName: String,
    var date: String,
    var content: String,
    var read: Int,
){
    fun getDateTime(): String{
        return date.substring(11)
    }
    fun getDateDate(): String{
        return date.substring(0,10)
    }
}