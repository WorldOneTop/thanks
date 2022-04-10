package com.hallym.hlth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallym.hlth.adapters.ChatInAdapter
import com.hallym.hlth.databinding.ActivityChatInBinding
import com.hallym.hlth.function.ChatController
import com.hallym.hlth.function.ChatDB
import com.hallym.hlth.function.Query
import com.hallym.hlth.models.Chatting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChatInActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatInBinding
    private lateinit var adapter: ChatInAdapter
    private lateinit var userName:String

    companion object{
        var userId:Int = 0
    }

    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(userId == intent.getIntExtra("userId",1)){
                when(intent.getStringExtra("category")){
                    "read" ->{
                        // UI 설정
                        adapter.setAllRead()
                    }"send" ->{
                    // UI 설정
                        adapter.addChat(Chatting(
                            userId,userName,intent.getStringExtra("date")!!,
                            intent.getStringExtra("content")!!,0,false
                        ))

                    binding.rvNotification.scrollToPosition(adapter.itemCount - 1)
                    // 서버 설정
                    Query().readChat(userId)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initRecyclerView()
        setData()
    }
    private fun initData(){
        userId = intent.getIntExtra("userId",0)
        userName = intent.getStringExtra("userName")!!
        setSupportActionBar(binding.toolbarChatIn)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = userName

        LocalBroadcastManager.getInstance(this).registerReceiver(
            mBroadcastReceiver, IntentFilter("Chatting")
        )
    }
    private fun initRecyclerView() {
        adapter = ChatInAdapter(applicationContext)

        binding.rvNotification.adapter = adapter
        binding.rvNotification.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvNotification.addOnLayoutChangeListener{ _, _, _, _, bottom, _, _, _, oldBottom->
                if (bottom < oldBottom){
                    Handler(Looper.getMainLooper()).postDelayed( // 바로 실행시 적용 안됨
                        { binding.rvNotification.scrollToPosition(adapter.itemCount - 1) },
                        30
                    )
                }
            }

        binding.rvNotification.scrollToPosition(adapter.itemCount - 1)
        binding.chatInEditButton.setOnClickListener{
            val content = binding.chatInEdit.text.toString()
            if(content.isNotEmpty()){
                binding.chatInEditRoot.isClickable = false
                binding.chatInEdit.isEnabled = false

                // 서버 전송
                Query().sendChat(userId,content){
                    // DB 저장
                    ChatController(ChatDB(applicationContext).writableDatabase).sendChat(userId,
                        content, it.getString("date"),userName)
                    // UI 설정
                    CoroutineScope(Dispatchers.Main).launch {
                        adapter.addChat(Chatting(userId,userName,it.getString("date"),content,1,true))
                        binding.chatInEdit.setText("")
                        binding.chatInEditRoot.isClickable = true
                        binding.chatInEdit.isEnabled = true
                        binding.rvNotification.scrollToPosition(adapter.itemCount - 1)
                    }
                }
            }
        }
    }

    private fun setData(){
        adapter.setData(
            ChatController(ChatDB(applicationContext).writableDatabase).getInRoom(userId)
        )
        binding.rvNotification.scrollToPosition(adapter.itemCount - 1)
        if(adapter.requireReadServer()){
            Query().readChat(userId)
            adapter.setAllRead()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        userId = 0
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}