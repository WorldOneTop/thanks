package com.hallym.hlth.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowEmptyFooterBinding
import com.hallym.hlth.databinding.RowMenuBinding
import com.hallym.hlth.databinding.RowMenuCategoryBinding
import com.hallym.hlth.viewholders.EmptyFooterViewHolder
import com.hallym.hlth.viewholders.MenuCategoryViewHolder
import com.hallym.hlth.viewholders.MenuViewHolder

class MenuAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: ArrayList<MenuValueObject> = arrayListOf()

    fun setData(data: ArrayList<MenuValueObject>) {
        val prev = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size + 1)
    }

    var onClickListener: ((id: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MenuViewHolder.VIEW_TYPE ->
                MenuViewHolder(
                    RowMenuBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )

            MenuCategoryViewHolder.VIEW_TYPE ->
                MenuCategoryViewHolder(
                    RowMenuCategoryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )

            else ->
                EmptyFooterViewHolder(
                    RowEmptyFooterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MenuViewHolder -> {
                holder.bind(data[position])
                holder.onClickListener = onClickListener
            }

            is MenuCategoryViewHolder -> {
                holder.bind(data[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position].type) {
            MenuValueObject.Companion.MenuType.menu -> MenuViewHolder.VIEW_TYPE
            MenuValueObject.Companion.MenuType.category -> MenuCategoryViewHolder.VIEW_TYPE
            else -> EmptyFooterViewHolder.VIEW_TYPE
        }
    }

}

class MenuValueObject(var id: String, var title: String, var iconId: Int?) {

    companion object {
        enum class MenuType {
            menu, category, footer
        }
    }

    var type: MenuType = MenuType.menu

    constructor(id: String, title: String) : this(id, title, null) {
        type = MenuType.category
    }
}