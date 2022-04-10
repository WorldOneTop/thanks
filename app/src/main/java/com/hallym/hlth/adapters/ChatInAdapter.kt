package com.hallym.hlth.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowChatInBinding
import com.hallym.hlth.models.Chatting
import com.hallym.hlth.viewholders.ChatInViewHolder
import com.hallym.hlth.viewholders.ChatViewHolder
import java.text.SimpleDateFormat

class ChatInAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: ArrayList<Chatting> = arrayListOf()

    fun setData(data: ArrayList<Chatting>) {
        val prev = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size + 1)
    }
    fun addChat(data: Chatting) {
        this.data.add(data)
        notifyItemInserted(this.data.size-1)
    }
    fun setAllRead(){
        var count = 0
        for(i in data.size-1 downTo 0){
            if(data[i].read == 0)
                break
            data[i].read = 0
            count++
        }
        Log.d("asd","허ㅘㄱ인 ${data.last().read}")
        notifyItemRangeChanged(data.size-count,count)
    }
    fun requireReadServer():Boolean{
        if(data.size==0)
            return false
        return !(data.last().isMe) && data.last().read != 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatInViewHolder(
            RowChatInBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var newDate = position==0
        if(!newDate){
            newDate = data[position].getDateDate() != data[position-1].getDateDate()
        }

        (holder as ChatInViewHolder).bind(data[position],newDate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}