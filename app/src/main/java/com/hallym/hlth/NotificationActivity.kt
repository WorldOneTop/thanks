package com.hallym.hlth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallym.hlth.adapters.NotificationAdapter
import com.hallym.hlth.adapters.NotificationObject
import com.hallym.hlth.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNotificationBinding
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notiBack.setOnClickListener{ onBackPressed() }
        initRecyclerView()
    }
    private fun initRecyclerView() {
        adapter = NotificationAdapter(applicationContext)
        // TODO: Replace with real data
        adapter.setData(
            arrayOf(
                NotificationObject(
                    "알림 제목 title1",
                    "내용 내용 text1",
                    R.drawable.ic_round_notifications_24
                ),
                NotificationObject(
                    "알림 제목 title2",
                    "내용 내용 text2",
                    R.drawable.ic_round_notifications_24
                ),
                NotificationObject(
                    "알림 제목 title3",
                    "내용 내용 text3",
                    R.drawable.ic_round_notifications_24
                ),
            )
        )

        binding.rvNotification.adapter = adapter
        binding.rvNotification.layoutManager = LinearLayoutManager(applicationContext)
    }
}