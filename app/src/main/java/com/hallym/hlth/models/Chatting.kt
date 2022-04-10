package com.hallym.hlth.models

class Chatting(
    var userId: Int, // 상대방 ID
    var userName: String, // 상대방 이름
    var date: String,
    var content: String,
    var read: Int, // 안읽은 수
    var isMe: Boolean,
){

    fun getDateTime(): String{
        return date.substring(11)
    }
    fun getDateDate(): String{
        return date.substring(0,10)
    }
}
data class ChatRoom(
    val userName: String,
    var userId: Int,
    var date: String,
    var content:String,
    var sumNoRead: Int,
){
    fun getDateTime(): String{
        return date.substring(11)
    }
    fun getDateDate(): String{
        return date.substring(0,10)
    }
}