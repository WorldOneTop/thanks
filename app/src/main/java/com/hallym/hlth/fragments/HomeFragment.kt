package com.hallym.hlth.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallym.hlth.MainActivity
import com.hallym.hlth.R
import com.hallym.hlth.adapters.HomeAdapter
import com.hallym.hlth.adapters.HomeGoalsValueObject
import com.hallym.hlth.databinding.FragmentHomeBinding
import com.hallym.hlth.models.Document

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbarHome)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = HomeAdapter(requireContext())

        // TODO: Replace with real data
        adapter.setData(
            arrayOf(
                "",
                HomeGoalsValueObject(
                    "🙇‍♂️ 감사합니다",
                    5,
                    1,
                    arrayOf(
                        Document(0, "고양이가 세상을 구한다", "감사", 2022, 2, 1, "2022/02/01", null),
                        Document(0, "고양이가 최고다", "감사", 2022, 2, 1, "2022/02/01", null)
                    )
                ),
                HomeGoalsValueObject(
                    "🙇‍♂️ 감사합니다",
                    1,
                    1,
                    arrayOf(
                        Document(0, "고양이는 귀엽다", "절약", 2022, 2, 1, "2022/02/01", null)
                    )
                )
            )
        )

        adapter.onCardClickListener = { _, position ->

        }

        adapter.onLinkClickListener = { url ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        binding.rvHome.adapter = adapter
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_notification -> {
                // TODO: Show notification
            }

            R.id.action_account -> {
                // TODO: Show account settings
            }

            else -> super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }
}