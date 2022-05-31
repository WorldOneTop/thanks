package com.hallym.hlth

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.hallym.hlth.databinding.ActivitySettingBinding
import com.hallym.hlth.function.LoginStorage
import com.hallym.hlth.function.Setting
import java.io.File

class SettingActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySettingBinding
    private lateinit var setting:Setting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSetting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.menu_setting)

        setting = Setting(this)
        initView()
        setListener()
    }
    private fun initView(){

        binding.settingColorTitle.text = resources.getStringArray(R.array.setting_color)[Setting.color]
        binding.settingAppLock.isChecked = Setting.isLock
        if(Setting.isLock){
            binding.settingAppLockLayout.visibility = View.VISIBLE
            binding.settingFinger.isChecked = Setting.isFingerLock
        }
        binding.settingAutoLogin.isChecked = Setting.isAutoLogin

        binding.settingFirstPageTitle.text = resources.getStringArray(R.array.first_page)[Setting.firstPage]
        binding.settingReceiveNoti.isChecked = Setting.isRecvNoti
        if(Setting.isRecvNoti){
            binding.settingReceiveNotiLayout.visibility = View.VISIBLE
            binding.settingNotiDaily.isChecked = Setting.isRecvDailyNoti
            binding.settingChat.isChecked = Setting.isRecvChat
        }
    }


    private fun setListener(){
        binding.settingColor.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.setting_color))
            builder.setItems(R.array.setting_color){ _, which ->
                setting.setColor(which)
                binding.settingColorTitle.text = resources.getStringArray(R.array.setting_color)[which]
            }

            builder.create().show()
        }
        binding.settingAppLock.setOnCheckedChangeListener{ _, checked ->
            setting.setLock(checked)
            if(checked){
                binding.settingAppLockLayout.visibility = View.VISIBLE
            }else{
                binding.settingAppLockLayout.visibility = View.GONE
                setting.setPin(null)
                setting.setFingerLock(false)
                binding.settingFinger.isChecked = false
            }
        }
        binding.settingPin.setOnClickListener{
            val intent = Intent(this, LockActivity::class.java)
            intent.putExtra("isChange",true)
            startActivity(intent)
        }
        binding.settingFinger.setOnCheckedChangeListener{ _, checked ->
            if(!checked){
                setting.setFingerLock(false)
            }
            else{
                val executor = ContextCompat.getMainExecutor(this)
                val biometricPrompt = BiometricPrompt(this, executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(errorCode: Int,
                                                           errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            Toast.makeText(applicationContext,errString.toString(), Toast.LENGTH_SHORT).show()
                            binding.settingFinger.isChecked = false
                        }

                        override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            setting.setFingerLock(true)
                        }
                    })

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for my app")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Use account password")
                    .build()

                biometricPrompt.authenticate(promptInfo)
            }
        }
        binding.settingAutoLogin.setOnCheckedChangeListener{ _, checked ->
            setting.setAutoLogin(checked)

        }
        binding.settingFirstPage.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.setting_first_page))
            builder.setItems(R.array.first_page){ _, which ->
                setting.setFirstPage(which)
                binding.settingFirstPageTitle.text = resources.getStringArray(R.array.first_page)[which]
            }

            builder.create().show()

        }
        binding.settingReceiveNoti.setOnCheckedChangeListener{ _, checked ->
            setting.setRecvNoti(checked)
            if(checked){
                binding.settingReceiveNotiLayout.visibility = View.VISIBLE
            }else{
                binding.settingReceiveNotiLayout.visibility = View.GONE
                setting.setRecvDailyNoti(false)
                binding.settingNotiDaily.isChecked = false
                setting.setNotiCategory(null)
                setting.setRecvChat(false)
                binding.settingChat.isChecked = false
            }

        }
        binding.settingNotiDaily.setOnCheckedChangeListener{ _, checked ->
            setting.setRecvDailyNoti(checked)
        }
        binding.settingNotiCategory.setOnClickListener{

        }
        binding.settingChat.setOnCheckedChangeListener{ _, checked ->
            setting.setRecvChat(checked)
        }
        binding.settingOperation.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hallym.ac.kr/hallym_univ/sub07/cP2/tab2")))
        }
        binding.settingPrivateOperation.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hallym.ac.kr/hallym_univ/sub07/cP2/tab2")))
        }
        binding.settingHelp.setOnClickListener{

        }
        binding.settingQnA.setOnClickListener{

        }
        binding.settingBug.setOnClickListener{

        }
        binding.settingRemoveData.setOnClickListener{
            removeDialog()
        }
    }

    private fun removeDialog(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.setting_remove_data))
            .setMessage(getString(R.string.setting_remove_data_check))
            .setPositiveButton("ok") { _: DialogInterface, _: Int ->
                val cache: File = applicationContext.cacheDir
                val appDir = cache.parent?.let { it1 -> File(it1) }
                if (appDir?.exists() == true) {
                    val children: Array<String> = appDir.list() as Array<String>
                    for (s in children) {
                        removeProcess(File(appDir, s))
                    }
                }
                applicationContext.getSharedPreferences(Setting.FILE_PATH, MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply()
                File(LoginStorage.FILE_PATH).delete()
                finishAffinity()
            }
            .create()
            .show()
    }
    private fun removeProcess(dir:File?):Boolean{
        if (dir?.isDirectory == true) {
            val children: Array<String> = dir.list() as Array<String>
            for (i in children.indices) {
                val success: Boolean = removeProcess(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir?.delete() == true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        setting.apply()
        super.onPause()
    }
}

