package com.hallym.hlth.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.hallym.hlth.*
import com.hallym.hlth.adapters.MenuAdapter
import com.hallym.hlth.adapters.MenuValueObject
import com.hallym.hlth.databinding.FragmentMenuBinding
import com.hallym.hlth.function.LoginStorage

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
        data.add(MenuValueObject("cat_account", getString(R.string.menu_tab_user)))
        data.add(MenuValueObject("account_manage", getString(R.string.menu_manage_account), R.drawable.ic_round_account_circle_24))
        data.add(MenuValueObject("cat_user", getString(R.string.menu_tab_member)))
        data.add(MenuValueObject("register_mentee", getString(R.string.menu_apply_mentee), R.drawable.ic_round_account_circle_24))
        data.add(MenuValueObject("register_mentor", getString(R.string.menu_apply_mentor), R.drawable.ic_round_account_circle_24))
        data.add(MenuValueObject("manager_mentee", getString(R.string.menu_manage_mentee), R.drawable.ic_round_account_circle_24))
        data.add(MenuValueObject("cat_app", getString(R.string.menu_tab_app)))
        data.add(MenuValueObject("app_setting", getString(R.string.menu_setting), R.drawable.ic_round_account_circle_24))
        data.add(MenuValueObject("app_info", getString(R.string.menu_app_info), R.drawable.ic_round_account_circle_24))
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
            when (id) {
                "account_manage" -> {
                    startActivity(Intent(requireContext(),UserInfoActivity::class.java))
                }
                "register_mentor" -> {
                    startApplyMen(true)
                }
                "register_mentee" -> {
                    startApplyMen(false)
                }
                "app_setting" ->{
                    startActivity(Intent(requireContext(),SettingActivity::class.java))
                }
                "app_info" -> {
                    Toast.makeText(requireContext(), "v 1.0.1 최신?", Toast.LENGTH_SHORT).show()
                }
                "manager_mentee" -> {
                    if(LoginStorage.status != 4)
                        Toast.makeText(requireContext(),getString(R.string.menu_manageMen_noMentor),Toast.LENGTH_LONG).show()
                    else
                        startActivity(Intent(requireContext(),MenteeManageActivity::class.java))
                }

            }
//            Toast.makeText(requireContext(), "$id clicked", Toast.LENGTH_SHORT).show()
        }

    }

    private fun startApplyMen(isMentor:Boolean){
        when(LoginStorage.status){
            1 ->{
                val intent = Intent(requireContext(), ApplyMenActivity::class.java)
                intent.putExtra("isMentor",isMentor)
                startActivity(intent)
            }
            2 -> Toast.makeText(requireContext(),getString(R.string.menu_applyMentee_already),Toast.LENGTH_LONG).show()
            3 -> Toast.makeText(requireContext(),getString(R.string.menu_applyMentor_already),Toast.LENGTH_LONG).show()
            else -> Toast.makeText(requireContext(),getString(R.string.menu_applyMen_already),Toast.LENGTH_LONG).show()
        }
    }
}