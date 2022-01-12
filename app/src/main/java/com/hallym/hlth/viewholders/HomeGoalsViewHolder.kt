package com.hallym.hlth.viewholders

import android.animation.ObjectAnimator
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.R
import com.hallym.hlth.adapters.HomeGoalsLineAdapter
import com.hallym.hlth.adapters.HomeGoalsValueObject
import com.hallym.hlth.databinding.RowHomeGoalsBinding

class HomeGoalsViewHolder(
    private val viewPool: RecyclerView.RecycledViewPool,
    private val binding: RowHomeGoalsBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = R.layout.row_home_goals
    }

    private val adapter = HomeGoalsLineAdapter(itemView.context)
    var onClickListener: ((View) -> Unit)? = null

    init {
        binding.rvRowHomeGoals.adapter = adapter

        val layoutManager = LinearLayoutManager(itemView.context)
        layoutManager.recycleChildrenOnDetach = true
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rvRowHomeGoals.layoutManager = layoutManager
        binding.rvRowHomeGoals.setRecycledViewPool(viewPool)
        binding.rvRowHomeGoals.suppressLayout(true)

    }

    fun bind(data: HomeGoalsValueObject) {
        binding.txtRowHomeGoalsTitle.text = data.title
        binding.progressRowHomeGoals.max = data.max
        binding.progressRowHomeGoals.progress = data.progress

        if (data.progress >= data.max) {
            binding.imgRowHomeGoalsProgressDone.visibility = View.VISIBLE
            binding.layoutRowHomeGoalsProgressBar.visibility = View.GONE

            val avd = ResourcesCompat.getDrawable(
                itemView.context.resources,
                R.drawable.avd_anim_done,
                null
            ) as AnimatedVectorDrawable
            binding.imgRowHomeGoalsProgressDone.setImageDrawable(avd)
            avd.start()

        } else {
            binding.imgRowHomeGoalsProgressDone.visibility = View.GONE
            binding.layoutRowHomeGoalsProgressBar.visibility = View.VISIBLE
            binding.txtRowHomeGoalsProgressMax.text = data.max.toString()
            binding.txtRowHomeGoalsProgress.text = data.progress.toString()
        }

        adapter.setData(data.documents)

        binding.cardRowHomeGoals.setOnClickListener { v ->
            onClickListener?.let { it(v) }
        }
    }
}