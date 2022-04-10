package com.hallym.hlth.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.hallym.hlth.ChatInActivity
import com.hallym.hlth.MainActivity
import com.hallym.hlth.NotificationActivity
import com.hallym.hlth.adapters.ChatAdapter
import com.hallym.hlth.adapters.HomeAdapter
import com.hallym.hlth.databinding.FragmentChatBinding
import com.hallym.hlth.function.ChatController
import com.hallym.hlth.function.ChatDB
import com.hallym.hlth.function.Query
//import com.hallym.hlth.function.ChatStorage
import com.hallym.hlth.models.Chatting
import java.util.*

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var adapter: ChatAdapter
    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(intent.getStringExtra("category") == "send"){
                adapter.addChatting(
                    intent.getIntExtra("userId",0), intent.getStringExtra("date").toString(),intent.getStringExtra("content").toString(),intent.getStringExtra("name").toString()
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbarChat)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)


        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ChatAdapter(requireContext())

        binding.rvChat.addItemDecoration(DividerItemDecoration(requireContext(),VERTICAL))
        binding.rvChat.adapter = adapter
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())

        val intent = Intent( requireContext(),ChatInActivity::class.java )
        adapter.onClickListener = { userId,userName ->
            intent.putExtra("userId",userId)
            intent.putExtra("userName",userName)
            startActivity(intent)
        }

    }

    override fun onStart() {
        adapter.setData(ChatController(ChatDB(requireContext()).readableDatabase).lastChatList())
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            mBroadcastReceiver, IntentFilter("Chatting"))
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mBroadcastReceiver)
    }
}
