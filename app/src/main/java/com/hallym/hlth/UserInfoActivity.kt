package com.hallym.hlth

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.hallym.hlth.databinding.ActivityUserInfoBinding
import com.hallym.hlth.function.Query
import com.hallym.hlth.function.Setting
import com.hallym.hlth.function.LoginStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setData()
        initListener()
    }
    private fun initView(){
        binding.infoId.text = LoginStorage.id
        binding.infoThanksCount.text = "0"
        binding.infoSaveCount.text = "0"
        binding.infoKindCount.text = "0"
        binding.infoBookCount.text = "0"
        binding.infoName.backgroundTintList = applicationContext.getColorStateList(Setting.getIndexToColor(Setting.color))

        dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(ProgressBar(this))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnCancelListener { this.finish() }
        dialog.show()

    }
    private fun setData(){
        Query().getAccountInfo {
            CoroutineScope(Main).launch {
                val data = it.getJSONObject("data")
                val documents = data.getJSONArray("documents")
                var sumDocument = 0
                val date = SimpleDateFormat("yyyy-MM-dd").parse(data.getString("registerDate")).time
                val now = Calendar.getInstance().time.time


                binding.infoName.text = data.getString("name").substring(1)
                binding.infoFullName.text = data.getString("name")
                binding.infoActivityDays.text =  ((now - date) / (60 * 60 * 24 * 1000)).toString()
                binding.infoSignupdate.text  = data.getString("registerDate")
                for(i in 0 until  documents.length()){
                    when(documents.getJSONObject(i).getInt("docType")) {
                        0 -> binding.infoThanksCount.text = documents.getJSONObject(i).getInt("count").toString()
                        1 -> binding.infoSaveCount.text = documents.getJSONObject(i).getInt("count").toString()
                        2 -> binding.infoKindCount.text = documents.getJSONObject(i).getInt("count").toString()
                        3 -> binding.infoBookCount.text = documents.getJSONObject(i).getInt("count").toString()
                    }
                    sumDocument += documents.getJSONObject(i).getInt("count")
                }
                binding.infoDocCount.text = sumDocument.toString()
                dialog.dismiss()
            }
        }

    }
    private fun initListener(){
        binding.infoLogout.setOnClickListener {
            LoginStorage(this).saveData("","",1)
            val setting = Setting(this)
            setting.setAutoLogin(false)
            setting.apply()

            finishAffinity()
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding.infoClose.setOnClickListener {
            finish()
        }
    }

}