package com.hallym.hlth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import com.hallym.hlth.databinding.ActivityPinactivityBinding
import com.hallym.hlth.function.Setting

class PINActivity : AppCompatActivity() {
    lateinit var binding:ActivityPinactivityBinding
    var isChange:Boolean = false
    var checkPin:Boolean = false
    var loginSuccess:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isChange = intent.getBooleanExtra("isChange",false)
        loginSuccess = intent.getBooleanExtra("loginSuccess",false)
        initView()
        initListener()

    }
    private fun initView(){
        if(isChange && Setting.pin != null){
            binding.pinTitle.text = getString(R.string.pin_old_title)
        }
        val passwordTransformation =  object: PasswordTransformationMethod() {
            override fun getTransformation(
                source: CharSequence?,
                view: View?
            ): CharSequence {
                return super.getTransformation(source, view).replace(Regex("\u2022"), "●")
            }
        }
        binding.pinText.transformationMethod = passwordTransformation
    }

    private fun initListener(){
        for(child in binding.pinInpuLayout.children){
            child.setOnClickListener{
                val char = (it as TextView).text
                if(char == "OK"){
                    submit(binding.pinText.text.toString())
                }else if(char == "←"){
                    if(binding.pinText.length()>0){
                        binding.pinText.setText(binding.pinText.text.substring(0,binding.pinText.length()-1))
                    }
                }else{
                    binding.pinText.setText("${binding.pinText.text}$char")
                }
            }
        }
        binding.pinRemove.setOnClickListener{
            val setting = Setting(this)
            setting.setPin(null)
            setting.apply()
            this.finish()
        }
    }

    private fun submit(text:String){
        if(text.length<4){
            Toast.makeText(this,getString(R.string.pin_4_letter),Toast.LENGTH_SHORT).show()
        }
        else if(!isChange){
            if(Setting.pin == text) {
                if(loginSuccess){
                    startActivity(Intent(this@PINActivity, MainActivity::class.java))
                }else{
                    startActivity(Intent(this@PINActivity, LoginActivity::class.java))
                }
                this.finish()
            }else{
                Toast.makeText(this,getString(R.string.pin_fail),Toast.LENGTH_SHORT).show()
            }
        }else if(!checkPin){
            if(Setting.pin == null){
                changedPIN(text)
            }
            else if(Setting.pin == text){
                checkPin = true
                binding.pinText.setText("")
                binding.pinTitle.setText(getString(R.string.pin_new_title))
                binding.pinRemove.visibility = View.VISIBLE
            }else{
                Toast.makeText(this,getString(R.string.pin_fail),Toast.LENGTH_SHORT).show()
            }
        }else{
            changedPIN(text)
        }
    }
    private fun changedPIN(text:String){
        val setting = Setting(this)
        setting.setPin(text)
        setting.apply()
        Toast.makeText(this,getString(R.string.pin_update),Toast.LENGTH_LONG).show()
        this.finish()
    }
}