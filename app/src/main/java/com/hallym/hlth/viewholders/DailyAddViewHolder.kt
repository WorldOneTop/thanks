package com.hallym.hlth.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.RowDailyAddBinding

class DailyAddViewHolder(private val binding: RowDailyAddBinding): RecyclerView.ViewHolder(binding.root) {
    var onClickListener: (() -> Unit)? = null

    fun bind(position:String, isVisibility:Boolean){
        binding.root.visibility =  if (isVisibility) View.VISIBLE else View.GONE
        binding.dailyAddImage.setOnClickListener {
            onClickListener?.let { it() }
        }
    }

}