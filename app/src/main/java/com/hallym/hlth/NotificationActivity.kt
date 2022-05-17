package com.hallym.hlth

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallym.hlth.adapters.NotificationAdapter
import com.hallym.hlth.databinding.ActivityNotificationBinding
import com.hallym.hlth.function.Query
import com.hallym.hlth.models.Notice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNotificationBinding
    private lateinit var adapter: NotificationAdapter
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarNotification)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.action_notification)

        initView()
        initData()
    }
    private fun initView(){
        adapter = NotificationAdapter(applicationContext, intent.getIntExtra("link",-1))
        intent.removeExtra("link")

        binding.rvNotification.adapter = adapter
        binding.rvNotification.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvNotification.addItemDecoration(DividerItemDecoration(applicationContext,1))

        if(Notice.noticeList == null) {
            dialog = Dialog(this)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(ProgressBar(this))
            dialog.setCanceledOnTouchOutside(false)
            dialog.setOnCancelListener { this.finish() }
            dialog.show()
        }
    }
    private fun initData(){
        if(Notice.noticeList != null){
            adapter.setData(Notice.noticeList!!)
            linkView(intent.getIntExtra("link",-1))
        }else{
            Query().getNoti{
                val data = it.getJSONArray("data")
                Notice.noticeList = ArrayList<Notice>()
                for( i in 0 until data.length()){
                    Notice.noticeList!!.add(Notice(
                        data.getJSONObject(i).getInt("id"),
                        data.getJSONObject(i).getString("title"),
                        data.getJSONObject(i).getString("content"),
                        data.getJSONObject(i).getString("registerDate"),
                        Notice.iconNotice
                    ))
                }
                CoroutineScope(Dispatchers.Main).launch {
                    adapter.setData(Notice.noticeList!!)
                    linkView(intent.getIntExtra("link",-1))
                    dialog.dismiss()
                }
            }
        }
    }
    private fun linkView(id:Int){
        if(id != -1){
            for(i in Notice.noticeList!!.count()-1 .. 0){
                if(Notice.noticeList!![i].id == id)
                    binding.rvNotification.findViewHolderForAdapterPosition(i)?.itemView?.performClick()

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}