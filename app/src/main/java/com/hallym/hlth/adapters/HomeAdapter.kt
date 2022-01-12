package com.hallym.hlth.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowEmptyFooterBinding
import com.hallym.hlth.databinding.RowHomeGoalsBinding
import com.hallym.hlth.databinding.RowHomeLinkBinding
import com.hallym.hlth.models.Document
import com.hallym.hlth.viewholders.EmptyFooterViewHolder
import com.hallym.hlth.viewholders.HomeGoalsViewHolder
import com.hallym.hlth.viewholders.HomeLinkViewHolder

class HomeAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val recyclerViewPool = RecyclerView.RecycledViewPool()

    private val data: ArrayList<Any> = arrayListOf()

    fun setData(data: Array<Any>) {
        val prev = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size)
    }

    fun updateData(position: Int, newData: Any) {
        this.data[position] = newData
        notifyItemChanged(position)
    }

    var onCardClickListener: ((view: View, position: Int) -> Unit)? = null
    var onLinkClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HomeLinkViewHolder.VIEW_TYPE ->
                HomeLinkViewHolder(
                    RowHomeLinkBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )

            HomeGoalsViewHolder.VIEW_TYPE ->
                HomeGoalsViewHolder(
                    this.recyclerViewPool,
                    RowHomeGoalsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )

            else -> EmptyFooterViewHolder(
                RowEmptyFooterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = this.data[position]

        if (holder is HomeGoalsViewHolder) {
            if (data is HomeGoalsValueObject) {
                holder.bind(data)
                holder.onClickListener = { view ->
                    onCardClickListener?.let { it(view, position) }
                }
            }
        } else if (holder is HomeLinkViewHolder) {
            holder.bind()
            holder.onLinkClickedListener = { url ->
                onLinkClickListener?.let { it(url) }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is HomeGoalsValueObject -> HomeGoalsViewHolder.VIEW_TYPE
            else -> HomeLinkViewHolder.VIEW_TYPE
        }
    }
}

class HomeGoalsValueObject(
    var title: String,
    var max: Int,
    var progress: Int,
    var documents: Array<Document>
)