package com.hallym.hlth.viewholders

import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowChatBinding
import com.hallym.hlth.models.ChatRoom
import com.hallym.hlth.models.Chatting
import java.text.SimpleDateFormat
import java.util.*

class ChatViewHolder(private val binding: RowChatBinding) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
    var onClickListener: ((userId: Int,userName: String) -> Unit)? = null
    var onContextMenuItemClickListener: ((userId: Int) -> Unit)? = null
    private val now = SimpleDateFormat("yyyy.M.dd", Locale.getDefault()).format(Calendar.getInstance().time)

    private var userId:Int? = null
    init {
        itemView.setOnCreateContextMenuListener(this)
    }

    fun bind(data: ChatRoom) {
        userId = data.userId
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

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu?.add(0, 0, 0, "채팅방 나가기")?.setOnMenuItemClickListener {
            userId?.let { it1 -> onContextMenuItemClickListener?.invoke(it1) }
            true
        }
    }
}