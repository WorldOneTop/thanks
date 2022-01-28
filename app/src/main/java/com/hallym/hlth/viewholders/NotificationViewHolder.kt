package com.hallym.hlth.viewholders


import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.R
import com.hallym.hlth.adapters.MenuValueObject
import com.hallym.hlth.adapters.NotificationObject
import com.hallym.hlth.databinding.RowNotificationBinding

class NotificationViewHolder(private val binding: RowNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: NotificationObject) {
        binding.rowNotiIcon.setImageResource(data.iconId)
        binding.rowNotiTitle.text = data.title
        binding.rowNotiText.text = data.text

    }
}