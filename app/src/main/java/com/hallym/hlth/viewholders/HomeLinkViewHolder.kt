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
            onLinkClickedListener?.let { it("https://become.hallym.ac.kr") }
        }
        binding.layoutRowHomeLinkBus.setOnClickListener {
            onLinkClickedListener?.let { it("https://www.hallym.ac.kr/hallym_univ/sub04/cP6/sCP3") }
        }
        binding.layoutRowHomeLinkShuttle.setOnClickListener {
            onLinkClickedListener?.let { it("https://www.hallym.ac.kr/hallym_univ/sub04/cP6/sCP4") }
        }
        binding.layoutRowHomeLinkMap.setOnClickListener {
            onLinkClickedListener?.let { it("https://www.hallym.ac.kr/hallym_univ/sub04/cP6/sCP1.html") }
        }
    }

}