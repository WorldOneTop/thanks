package com.hallym.hlth.viewholders


import android.animation.ObjectAnimator
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.R
import com.hallym.hlth.adapters.MenuValueObject
import com.hallym.hlth.databinding.RowNotificationBinding
import com.hallym.hlth.models.Notice

class NotificationViewHolder(private val binding: RowNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
    var isOpen:Boolean = false

    fun bind(data: Notice) {
        binding.rowNotiIcon.setImageResource(data.icon)
        binding.rowNotiTitle.text = data.title
        binding.rowNotiDate.text = data.date
        binding.rowNotiDetail.text = data.content
        binding.root.setOnClickListener {
            isOpen = !isOpen
            val currentDegree = binding.rowNotiArrow.rotation
            ObjectAnimator.ofFloat(binding.rowNotiArrow, View.ROTATION, currentDegree, currentDegree + 180f).setDuration(100).start()
            if(isOpen){
                binding.rowNotiDetail.visibility = View.VISIBLE
                binding.root.setBackgroundColor(Color.parseColor("#E2E4E8"))
            }else{
                binding.rowNotiDetail.visibility = View.GONE
                binding.root.setBackgroundColor(Color.parseColor("#F0F2F6"))
            }
        }
    }
}