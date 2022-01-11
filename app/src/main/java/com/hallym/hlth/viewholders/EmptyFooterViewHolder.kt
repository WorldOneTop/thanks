package com.hallym.hlth.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.R
import com.hallym.hlth.databinding.RowEmptyFooterBinding

class EmptyFooterViewHolder(private val binding: RowEmptyFooterBinding): RecyclerView.ViewHolder(binding.root) {
    companion object {
        const val VIEW_TYPE = R.layout.row_empty_footer
    }
}