package com.hallym.hlth.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowChatBinding
import com.hallym.hlth.models.Chatting
import com.hallym.hlth.viewholders.*

class ChatAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: ArrayList<Chatting> = arrayListOf()
    var onClickListener: ((chatRoom: Int,userName: String, userId: Int) -> Unit)? = null

    fun setData(data: Array<Chatting>) {
        val prev = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size + 1)
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

    }

    override fun getItemCount(): Int {
        return data.size
    }

}