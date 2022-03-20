package com.hallym.hlth.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowHomeGoalsLineBinding
import com.hallym.hlth.models.Document
import com.hallym.hlth.viewholders.HomeGoalsLineViewHolder

class HomeGoalsLineAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: ArrayList<Document> = arrayListOf()

    fun setData(documents: ArrayList<Document>) {
        val prev = data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        this.data.addAll(documents)
        notifyItemRangeInserted(0, documents.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeGoalsLineViewHolder(
            RowHomeGoalsLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as HomeGoalsLineViewHolder

        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return HomeGoalsLineViewHolder.VIEW_TYPE
    }
}
