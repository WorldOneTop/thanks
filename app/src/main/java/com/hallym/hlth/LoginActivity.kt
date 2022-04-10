package com.hallym.hlth

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hallym.hlth.databinding.ActivityLoginBinding
import com.hallym.hlth.function.InitData
import com.hallym.hlth.function.Query
import com.hallym.hlth.function.Setting
import com.hallym.hlth.function.LoginStorage
import com.hallym.hlth.models.Document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val pattern = Pattern.compile("20[0-9]{6}\$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActv()
    }
    private fun initActv(){
        binding.loginSubmit.setOnClickListener{
            if(checkForm()) {
                submit()
            }else{
                Toast.makeText(this, getString(R.string.login_empty), Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginPw.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (checkForm()) {
                    submit()
                } else {
                    Toast.makeText(this, getString(R.string.login_empty), Toast.LENGTH_SHORT).show()
                }
            }
            false
        }
    }
    private fun checkForm():Boolean{
        val id = binding.loginId.text.toString()
        if(id.isEmpty() || !pattern.matcher(id).matches()){
            return false
        }
        if(binding.loginPw.text.toString().isEmpty()){
            return false
        }
        return true
    }

    private fun submit(){
        Query().login(binding.loginId.text.toString(), binding.loginPw.text.toString()){
            try {
                val status = JSONObject(it)
                if(status.getString("status") == "OK"){
                    if(binding.loginAutoLogin.isChecked){
                        LoginStorage(this).saveData(binding.loginId.text.toString(),binding.loginPw.text.toString(),status.getInt("userStatus"))
                        val setting = Setting(this)
                        setting.setAutoLogin(true)
                        setting.apply()
                    }else{
                        LoginStorage(this).setData(binding.loginId.text.toString(),binding.loginPw.text.toString(),status.getInt("userStatus"))
                    }
                    InitData(this){
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                }else{
                    CoroutineScope(Dispatchers.Main).launch{
                        Toast.makeText(applicationContext,getString(R.string.login_fail),Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e:JSONException){
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"Can't connect to server.",Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}