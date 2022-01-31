package com.hallym.hlth.adapters

import android.content.Context
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

    fun setData(data: Array<Chatting>) {
        val prev = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size + 1)
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