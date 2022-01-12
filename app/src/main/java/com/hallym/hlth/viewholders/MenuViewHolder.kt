package com.hallym.hlth.viewholders

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.R
import com.hallym.hlth.adapters.MenuValueObject
import com.hallym.hlth.databinding.RowMenuBinding

class MenuViewHolder(private val binding: RowMenuBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = R.layout.row_menu
    }

    var onClickListener: ((id: String) -> Unit)? = null

    fun bind(data: MenuValueObject) {
        binding.txtRowMenuTitle.text = data.title

        if (data.iconId != null) {
            binding.imgRowMenuIcon.setImageDrawable(
                ResourcesCompat.getDrawable(
                    itemView.context.resources,
                    data.iconId!!,
                    null
                )
            )
        } else {
            binding.layoutRowMenuIcon.visibility = View.GONE
        }

        binding.cardRowMenu.setOnClickListener { onClickListener?.let { it(data.id) } }
    }
}