package com.hallym.hlth.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.R
import com.hallym.hlth.databinding.RowHomeLinkBinding

class HomeLinkViewHolder(val binding: RowHomeLinkBinding): RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = R.layout.row_home_link
    }

    var onLinkClickedListener: ((String) -> Unit)? = null

    fun bind() {
        binding.layoutRowHomeLinkBecome.setOnClickListener {
            onLinkClickedListener?.let { it("https://www.hallym.ac.kr") }
        }
        binding.layoutRowHomeLinkBus.setOnClickListener {
            onLinkClickedListener?.let { it("https://www.hallym.ac.kr") }
        }
        binding.layoutRowHomeLinkShuttle.setOnClickListener {
            onLinkClickedListener?.let { it("https://www.hallym.ac.kr") }
        }
        binding.layoutRowHomeLinkMap.setOnClickListener {
            onLinkClickedListener?.let { it("https://www.hallym.ac.kr") }
        }
    }

}