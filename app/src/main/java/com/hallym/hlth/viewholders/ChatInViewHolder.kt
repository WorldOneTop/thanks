package com.hallym.hlth.viewholders

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowChatInBinding
import com.hallym.hlth.models.Chatting
import java.text.SimpleDateFormat
import java.util.*

class ChatInViewHolder(private val binding: RowChatInBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Chatting, newDate: Boolean = false ) {
        if(newDate){
            binding.rowChatInDate.text = data.getDateDate()
            binding.rowChatInDateLayout.visibility = View.VISIBLE
        }else{
            binding.rowChatInDateLayout.visibility = View.GONE
        }
        if(data.isMe){
            binding.rowChatInContent.text = data.content
            binding.rowChatInTime.text = data.getDateTime()
            if(data.read != 0)
                binding.rowChatInRead.text = "1"
            else
                binding.rowChatInRead.text = ""
            binding.rowChatYouLayout.visibility = View.GONE
            binding.rowChatMyLayout.visibility = View.VISIBLE
        }else{
            binding.rowChatInName.text = data.userName
            binding.rowChatInUContent.text = data.content
            binding.rowChatInUTime.text = data.getDateTime()
            if(data.read != 0)
                binding.rowChatInURead.text = "1"
            else
                binding.rowChatInRead.text = ""
            binding.rowChatYouLayout.visibility = View.VISIBLE
            binding.rowChatMyLayout.visibility = View.GONE
        }

    }
}