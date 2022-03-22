package com.hallym.hlth.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.*
import com.hallym.hlth.models.Chatting
import com.hallym.hlth.models.Document
import com.hallym.hlth.viewholders.*

class DailyAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: ArrayList<String> = arrayListOf()

    fun setData(position: Int) {
        val prev = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        for(doc in Document.todayDataType[position]!!){
            this.data.add(doc.content)
        }
        notifyItemRangeInserted(0, data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DailyViewHolder(
            RowDailyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DailyViewHolder).bind(data[position])
    }
    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }
}