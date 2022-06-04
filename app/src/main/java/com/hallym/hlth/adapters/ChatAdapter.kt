package com.hallym.hlth.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowChatBinding
import com.hallym.hlth.models.ChatRoom
import com.hallym.hlth.models.Chatting
import com.hallym.hlth.viewholders.*

class ChatAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: ArrayList<ChatRoom> = arrayListOf()
    var onClickListener: ((chatRoom: Int,userName: String) -> Unit)? = null
    var onContextMenuItemClickListener: ((userId: Int) -> Unit)? = null

    fun setData(data: ArrayList<ChatRoom>) {
        val prev = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size + 1)
    }
    fun addChatting(userId:Int, date:String, content:String, name:String){
        for(i in 0 until data.size){
            if(data[i].userId == userId){
                data[i].content = content
                data[i].sumNoRead += 1
                data[i].date = date
                if(i==0)
                    notifyItemChanged(0)
                else
                    notifyItemMoved(i,0)
                return
            }
        }
        data.add(0,ChatRoom(
            name,userId,date,content,1
        ))
        notifyItemInserted(0)
    }
    fun deleteChatRoom(userId:Int){
        for(i in 0 until data.size){
            if(data[i].userId == userId)
                data.removeAt(i)
                notifyItemRemoved(i)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder(
            RowChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChatViewHolder).bind(data[position])
        holder.onClickListener = onClickListener
        holder.onContextMenuItemClickListener = onContextMenuItemClickListener

    }

    override fun getItemCount(): Int {
        return data.size
    }

}