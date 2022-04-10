package com.hallym.hlth.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowChatBinding
import com.hallym.hlth.models.ChatRoom
import com.hallym.hlth.models.Chatting
import java.text.SimpleDateFormat
import java.util.*

class ChatViewHolder(private val binding: RowChatBinding) : RecyclerView.ViewHolder(binding.root) {
    var onClickListener: ((userId: Int,userName: String) -> Unit)? = null
    val now = SimpleDateFormat("yyyy.M.dd").format(Calendar.getInstance().time)

    fun bind(data: ChatRoom) {
        binding.rowChatContent.text = data.content
        binding.rowChatName.text = data.userName
        binding.rowChatRead.text = data.sumNoRead.toString()

        binding.rowChatDate.text = if(now != data.getDateDate())
            data.getDateDate()
        else
            data.getDateTime()
        if(data.sumNoRead == 0)
            binding.rowChatRead.visibility = View.INVISIBLE
        else
            binding.rowChatRead.visibility = View.VISIBLE
        binding.rowChatRoot.setOnClickListener { onClickListener?.let { it(data.userId,data.userName) } }

    }
}