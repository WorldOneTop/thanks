package com.hallym.hlth.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowNotificationBinding
import com.hallym.hlth.models.Notice
import com.hallym.hlth.viewholders.EmptyFooterViewHolder
import com.hallym.hlth.viewholders.MenuCategoryViewHolder
import com.hallym.hlth.viewholders.MenuViewHolder
import com.hallym.hlth.viewholders.NotificationViewHolder

class NotificationAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: ArrayList<Notice> = arrayListOf()

    fun setData(data: ArrayList<Notice>) {
        val prev = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size + 1)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotificationViewHolder(
            RowNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
        ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NotificationViewHolder).bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
