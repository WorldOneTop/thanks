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
    var onClickListener: (() -> Unit)? = null

    fun setData(position: Int) {
        val prev = this.data.size + 2
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        for(doc in Document.todayDataType[position]!!){
            this.data.add(doc.content)
        }
        this.data.add(position.toString())
        notifyItemRangeInserted(0, data.size + 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            0 -> DailyViewHolder(
                RowDailyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                ))
            1 -> DailyAddViewHolder(
                RowDailyAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                ))
            else -> EmptyFooterViewHolder(
                RowEmptyFooterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                ))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DailyViewHolder -> {
                holder.bind(data[position])
            }
            is DailyAddViewHolder -> {
                holder.bind(data.last(),isRequiredAdd())
                holder.onClickListener = onClickListener
            }
        }
    }
    private fun isRequiredAdd():Boolean{
        if(data.last() == "0"){
            if(data.size >= 5 + 1) return false
        }else{
            if(data.size >= 1 + 1) return false
        }
        return true
    }
    override fun getItemCount(): Int {
        return data.size+1
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            data.size-1 -> 1
            data.size -> 2
            else -> 0
        }
    }
}