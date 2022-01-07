package com.hallym.hlth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.hallym.hlth.databinding.ActivityStartingBinding

class StartingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start Activity after few seconds
        Handler(Looper.getMainLooper()).postDelayed({

            // TODO: 로그인이 되어 있지 않으면 로그인 Activity로 이동
            startActivity(Intent(this@StartingActivity, MainActivity::class.java))

        }, 1000)
    }
}