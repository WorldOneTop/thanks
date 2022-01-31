package com.hallym.hlth.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.hallym.hlth.ChatInActivity
import com.hallym.hlth.MainActivity
import com.hallym.hlth.NotificationActivity
import com.hallym.hlth.adapters.ChatAdapter
import com.hallym.hlth.adapters.HomeAdapter
import com.hallym.hlth.databinding.FragmentChatBinding
import com.hallym.hlth.models.Chatting
import java.util.*

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var adapter: ChatAdapter

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

        // TODO: Replace with real data
        adapter.setData(
            arrayOf(
                Chatting(0,0,"상대 이름", "2022.01.30 20:10","보낸 내용",1),
                Chatting(1,0,"상대 이름","2022.01.30 20:10","보낸 내용",10),
                Chatting(2,0,"다른상대","2022.01.29 20:10","보낸 내용22",100),
                Chatting(3,0,"다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대",
                    "2022.01.29 20:10","보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용",0),
                Chatting(3,0,"다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대다른상대",
                    "2021.01.29 20:10","보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용보낸 내용",1000),
            )
        )

        binding.rvChat.addItemDecoration(DividerItemDecoration(requireContext(),VERTICAL))
        binding.rvChat.adapter = adapter
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())

        var intent = Intent( requireContext(),ChatInActivity::class.java )
        adapter.onClickListener = { chatRoom,userName,userId ->
            intent.putExtra("chatRoom",chatRoom)
            intent.putExtra("userName",userName)
            intent.putExtra("userId",userId)
            startActivity(
                intent
            )

        }

    }
}

//class Chatting (
//    var userId: Int,
//    var userName: String,
//    var date: String,
//    var content: String,
//    var read: Int,
//)