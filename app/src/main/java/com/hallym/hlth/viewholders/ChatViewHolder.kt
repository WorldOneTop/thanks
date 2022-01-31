package com.hallym.hlth.viewholders

import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowChatBinding
import com.hallym.hlth.models.Chatting
import java.text.SimpleDateFormat
import java.util.*

class ChatViewHolder(private val binding: RowChatBinding) : RecyclerView.ViewHolder(binding.root) {
    var onClickListener: ((chatRoom: Int,userName: String, userId: Int) -> Unit)? = null
    val now = SimpleDateFormat("yyyy.M.dd").format(Calendar.getInstance().time)

    fun bind(data: Chatting) {
        binding.rowChatContent.text = data.content
        binding.rowChatName.text = data.userName
        binding.rowChatRead.text = data.read.toString()

        if(now != data.getDateDate())
            binding.rowChatDate.text = data.getDateDate()
        else
            binding.rowChatDate.text = data.getDateTime()


        if(data.read==0)
            binding.rowChatRead.visibility = View.INVISIBLE

        binding.rowChatRoot.setOnClickListener { onClickListener?.let { it(data.chatRoom,data.userName,data.userId) } }

    }
}