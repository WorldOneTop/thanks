package com.hallym.hlth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hallym.hlth.databinding.ActivityMainBinding
import com.hallym.hlth.fragments.DailyFragment
import com.hallym.hlth.fragments.HomeFragment
import com.hallym.hlth.fragments.ChatFragment
import com.hallym.hlth.fragments.MenuFragment
import com.hallym.hlth.function.Setting

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var dailyFragment: DailyFragment
    private lateinit var chatFragment: ChatFragment
    private lateinit var menuFragment: MenuFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragment()
        initView()
    }

    private fun initFragment() {
        homeFragment = HomeFragment.newInstance()
        dailyFragment = DailyFragment.newInstance()
        chatFragment = ChatFragment.newInstance()
        menuFragment = MenuFragment.newInstance()
    }

    private fun initView() {
        // Set first fragment
        when(Setting.firstPage){
            0 -> {changeFragment(homeFragment)
                binding.bnvMain.selectedItemId = R.id.action_home
            }
            1 -> {
                changeFragment(dailyFragment)
                binding.bnvMain.selectedItemId = R.id.action_daily
            }
            2 -> {
                changeFragment(chatFragment)
                binding.bnvMain.selectedItemId = R.id.action_chat
            }
            3 -> {
                changeFragment(menuFragment)
                binding.bnvMain.selectedItemId = R.id.action_menu
            }
            else -> {
                changeFragment(homeFragment)
                binding.bnvMain.selectedItemId = R.id.action_home
            }
        }

        // Set OnItemSelectedListener on BottomNavigationView
        binding.bnvMain.setOnItemSelectedListener { menu ->
            changeFragment(when (menu.itemId) {
                R.id.action_home -> homeFragment
                R.id.action_daily -> dailyFragment
                R.id.action_chat -> chatFragment
                R.id.action_menu -> menuFragment
                else -> homeFragment
            })
            true
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layoutMain, fragment)
        transaction.commit()
    }
}
