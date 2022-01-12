package com.hallym.hlth.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.R
import com.hallym.hlth.adapters.MenuValueObject
import com.hallym.hlth.databinding.RowMenuCategoryBinding

class MenuCategoryViewHolder(private val binding: RowMenuCategoryBinding): RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = R.layout.row_menu_category
    }

    fun bind(data: MenuValueObject) {
        binding.txtRowMenuCategoryTitle.text = data.title
    }
}