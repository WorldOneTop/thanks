package com.hallym.hlth.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.R
import com.hallym.hlth.databinding.RowHomeGoalsLineBinding
import com.hallym.hlth.models.Document

class HomeGoalsLineViewHolder(private val binding: RowHomeGoalsLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = R.layout.row_home_goals_line
    }

    fun bind(document: Document) {
        binding.txtRowHomeGoalsLine.text =
            if (document.title != null) document.title else document.content
    }
}