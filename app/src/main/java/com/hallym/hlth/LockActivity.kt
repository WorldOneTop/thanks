package com.hallym.hlth

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.hallym.hlth.databinding.ActivityLockactivityBinding
import com.hallym.hlth.function.Setting


class LockActivity : AppCompatActivity() {
    lateinit var binding: ActivityLockactivityBinding
    var isChange:Boolean = false
    var checkPin:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isChange = intent.getBooleanExtra("isChange",false)

        initView()
        initListener()
        if(!isChange && Setting.isFingerLock){
            showFingerLock()
        }
    }
    private fun initView(){
        if(!isChange && Setting.pin == null && Setting.isFingerLock){
            binding.pinFingerImage.visibility = View.VISIBLE
            binding.pinInpuLayout.visibility = View.GONE
            binding.pinTitle.text = getString(R.string.pin_check_finger)
        }else{
            binding.pinFingerImage.visibility = View.GONE
            binding.pinInpuLayout.visibility = View.VISIBLE
        }

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
            Toast.makeText(this,getString(R.string.pin_4_letter), Toast.LENGTH_SHORT).show()
        }
        else if(!isChange){
            if(Setting.pin == text) {
                this.finish()
            }else{
                Toast.makeText(this,getString(R.string.pin_fail), Toast.LENGTH_SHORT).show()
            }
        }else if(!checkPin){
            if(Setting.pin == null){
                changedPIN(text)
            }
            else if(Setting.pin == text){
                checkPin = true
                binding.pinText.setText("")
                binding.pinTitle.text = getString(R.string.pin_new_title)
                binding.pinRemove.visibility = View.VISIBLE
            }else{
                Toast.makeText(this,getString(R.string.pin_fail), Toast.LENGTH_SHORT).show()
            }
        }else{
            changedPIN(text)
        }
    }
    private fun changedPIN(text:String){
        val setting = Setting(this)
        setting.setPin(text)
        setting.apply()
        Toast.makeText(this,getString(R.string.pin_update), Toast.LENGTH_LONG).show()
        this.finish()

    }

    private fun showFingerLock(){
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,errString.toString(),Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    finish()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onBackPressed() {
        if(isChange){
            super.onBackPressed()
        }else{
            moveTaskToBack(true)
        }
    }
}

class LockProcessLifecycle private constructor() : LifecycleEventObserver {
    companion object {
        private var instance: LockProcessLifecycle? = null
        private lateinit var context: Context
        fun getInstance(_context: Context): LockProcessLifecycle {
            return instance ?: synchronized(this) {
                instance ?: LockProcessLifecycle().also {
                    context = _context
                    instance = it
                }
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if(Setting.isFingerLock || Setting.pin != null) {
            if (event == Lifecycle.Event.ON_RESUME) {
                context.startActivity(
                    Intent(context, LockActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                )
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
            }
        }
    }
}