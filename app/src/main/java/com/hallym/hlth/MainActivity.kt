package com.hallym.hlth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hallym.hlth.databinding.ActivityMainBinding
import com.hallym.hlth.fragments.CategoryFragment
import com.hallym.hlth.fragments.HomeFragment
import com.hallym.hlth.fragments.MeFragment
import com.hallym.hlth.fragments.MenuFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var homeFragment: HomeFragment
    private lateinit var categoryFragment: CategoryFragment
    private lateinit var meFragment: MeFragment
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
        categoryFragment = CategoryFragment.newInstance()
        meFragment = MeFragment.newInstance()
        menuFragment = MenuFragment.newInstance()
    }

    private fun initView() {
        // Set first fragment
        changeFragment(homeFragment)

        // Set OnItemSelectedListener on BottomNavigationView
        binding.bnvMain.setOnItemSelectedListener { menu ->
            changeFragment(when (menu.itemId) {
                R.id.action_home -> homeFragment
                R.id.action_category -> categoryFragment
                R.id.action_me -> meFragment
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
