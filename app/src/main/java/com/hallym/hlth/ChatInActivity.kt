package com.hallym.hlth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallym.hlth.adapters.ChatInAdapter
import com.hallym.hlth.adapters.NotificationAdapter
import com.hallym.hlth.databinding.ActivityChatInBinding
import com.hallym.hlth.models.Chatting
import java.util.*

class ChatInActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatInBinding
    private lateinit var adapter: ChatInAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initRecyclerView()
    }
    private fun initData(){
        binding.chatTitle.setOnClickListener{ onBackPressed() }
        binding.chatTitle.text = intent.getStringExtra("userName")


    }
    private fun initRecyclerView() {
        adapter = ChatInAdapter(applicationContext)
        // TODO: Replace with real data
        adapter.setData(
            arrayOf(
                Chatting(
                    0,
                    1,
                    "아무개",
                    "2022.01.29 15:15",
                    "말 한 내용ㅇ",
                    0
                ),
                Chatting(
                    0,
                    0,
                    "내이름",
                    "2022.1.29 15:15",
                    "내가 말 한 내용ㅇ",
                    0
                ),
                Chatting(
                    0,
                    0,
                    "내이름",
                    "2022.01.29 15:15",
                    "내가 말 한 내용ㅇ",
                    0
                ),
                Chatting(
                    0,
                    1,
                    "아무개",
                    "2022.01.29 15:15",
                    "말 한 내용ㅇ",
                    0
                ),
                Chatting(
                    0,
                    1,
                    "아무개",
                    "2022.01.29 15:15",
                    "길게 말 한 내용길게 말 한 내용길게 말 한 내용길게 말 한 내용길게 말 한 내용길게 말 한 내용길게 말 한 내용길게 말 한",
                    0
                ),
                Chatting(
                    0,
                    0,
                    "내이름",
                    "2022.01.29 15:15",
                    "내가 길게 말 한 내용내가 길게 말 한 내용내가 길게 말 한 내용내가 길게 말 한 내용내가 길게 말 한 내용내가 길게 말 한",
                    1
                ),
            )
        )

        binding.rvNotification.adapter = adapter
        binding.rvNotification.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvNotification.scrollToPosition(adapter.itemCount - 1)

    }
}