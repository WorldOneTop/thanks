package com.hallym.hlth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.hallym.hlth.MainActivity
import com.hallym.hlth.R
import com.hallym.hlth.adapters.MenuAdapter
import com.hallym.hlth.adapters.MenuValueObject
import com.hallym.hlth.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
    }

    private lateinit var binding: FragmentMenuBinding
    private lateinit var adapter: MenuAdapter
    private val data: ArrayList<MenuValueObject> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbarMenu)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        initData()
        initRecyclerView()
    }

    private fun initData() {
        this.data.clear()

        // TODO: Replace with real data
        data.add(MenuValueObject("cat_user", "회원 관리"))
        data.add(MenuValueObject("register_mentee", "멘티 신청", R.drawable.ic_round_account_circle_24))
        data.add(MenuValueObject("register_mentor", "멘토 신청", R.drawable.ic_round_account_circle_24))
        data.add(MenuValueObject("manager_mentee", "멘티 관리", R.drawable.ic_round_account_circle_24))
        data.add(MenuValueObject("cat_app", "애플리케이션"))
        data.add(MenuValueObject("app_info", "애플리케이션 정보", R.drawable.ic_round_account_circle_24))
        data.add(MenuValueObject("footer", ""))
    }

    private fun initRecyclerView() {
        adapter = MenuAdapter(requireContext())
        adapter.setData(this.data)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (data[position].type == MenuValueObject.Companion.MenuType.menu) 1
                else 2
            }
        }
        binding.rvMenu.layoutManager = layoutManager
        binding.rvMenu.adapter = adapter

        adapter.onClickListener = { id ->
            // TODO: click events
            when (id) {
                "register_mentee" -> {}
            }
            Toast.makeText(requireContext(), "$id clicked", Toast.LENGTH_SHORT).show()
        }

    }
}