package com.hallym.hlth.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowChatBinding
import com.hallym.hlth.databinding.RowDailyBinding
import com.hallym.hlth.databinding.RowHomeGoalsLineBinding

class DailyViewHolder(private val binding: RowDailyBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: String){
        binding.rowDailyText.text = data
    }
}