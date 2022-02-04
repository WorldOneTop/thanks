package com.hallym.hlth.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.hallym.hlth.R
import com.hallym.hlth.adapters.ChatAdapter
import com.hallym.hlth.adapters.DailyAdapter
import com.hallym.hlth.databinding.FragmentDailyBinding

class DailyFragment : Fragment() {

    private lateinit var binding: FragmentDailyBinding
    private lateinit var adapter: DailyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = DailyFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTab()
    }

    private fun initTab(){
        adapter = DailyAdapter(requireContext())

        binding.tabbarDaily.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                changeTab(tab.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
        changeTab(0)

        binding.rvDaily.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDaily.adapter = adapter
    }
    private fun changeTab(index: Int){
        when(index){
            1 ->{
                binding.dailyTitle.text = getString(R.string.title_save)
                binding.dailySubTitle.text = getString(R.string.daily_save_text)
                adapter.setData(arrayOf(),index.toString())
                adapter.onClickListener = {
                        position ->
                    Log.d("asdasd",position)
                }
            }
            2 ->{
                binding.dailyTitle.text = getString(R.string.title_kind)
                binding.dailySubTitle.text = getString(R.string.daily_kind_text)
                adapter.setData(arrayOf(),index.toString())
                adapter.onClickListener = {
                        position ->
                    Log.d("asdasd",position)
                }
            }
            3 ->{
                binding.dailyTitle.text = getString(R.string.title_book)
                binding.dailySubTitle.text = getString(R.string.daily_book_text)
                adapter.setData(arrayOf("대한민국의 국민이 되는 요건은 법률로 정한다. 모든 국민은 행위시의 법률에 의하여 범죄를 구성하지 아니하는 행위로 소추되지 아니하며, 동일한 범죄에 대하여 거듭 처벌받지 아니한다. 여자의 근로는 특별한 보호를 받으며, 고용·임금 및 근로조건에 있어서 부당한 차별을 받지 아니한다. 대통령은 제1항과 제2항의 처분 또는 명령을 한 때에는 지체없이 국회에 보고하여 그 승인을 얻어야 한다."
                    ),index.toString())
                adapter.onClickListener = {
                        position ->
                    Log.d("asdasd",position)
                }
            }
            else ->{
                binding.dailyTitle.text = getString(R.string.title_thanks)
                binding.dailySubTitle.text = getString(R.string.daily_thanks_text)
                adapter.setData(arrayOf("대한민국의 국민이 되는 요건은 법률로 정한다. 모든 국민은 행위시의 법률에 의하여 범죄를 구성하지 아니하는 행위로 소추되지 아니하며, 동일한 범죄에 대하여 거듭 처벌받지 아니한다. 여자의 근로는 특별한 보호를 받으며, 고용·임금 및 근로조건에 있어서 부당한 차별을 받지 아니한다. 대통령은 제1항과 제2항의 처분 또는 명령을 한 때에는 지체없이 국회에 보고하여 그 승인을 얻어야 한다."
                    ,"2","3","4"),index.toString())
                adapter.onClickListener = {
                        position ->
                    Log.d("asdasd",position)
                }
            }

        }


    }
}
