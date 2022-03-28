package com.hallym.hlth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hallym.hlth.databinding.ActivityStartingBinding
import android.content.DialogInterface

import android.content.ActivityNotFoundException
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.hallym.hlth.function.Query
import com.hallym.hlth.function.Setting
import com.hallym.hlth.function.LoginStorage
import com.hallym.hlth.models.Document
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class StartingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartingBinding
    private val PERMISSION_REQUEST_CODE = 1
    private var loginSuccess: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if(checkPermission()) {
            initData()
            startApp()
        }else{
            requestPermission()
        }
    }

    private fun initData(){
        LoginStorage(applicationContext).loadIdPw()
        Setting(applicationContext).loadData()
    }
    private fun startApp(){
        if(Setting.isAutoLogin){
            Query().login(LoginStorage.id!!, LoginStorage.pw!!){
                try {
                    val status = JSONObject(it)
                    if(status.getString("status") == "OK"){
                        loginSuccess = true
                        setTodayDocumentData()
                    }else{
                        startActivity()
                    }
                }catch (e:JSONException){
                    CoroutineScope(Dispatchers.Main).launch{
                        Toast.makeText(applicationContext,"Can't connect to server.",Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        }else{
            startActivity()
        }
    }

    private fun setTodayDocumentData(){
        Document.clearTodayData()
        Document.homeDataType = Document.todayDataType

        Query().getDoc(LoginStorage.id.toString(),Query.now()){
            try {
                val documents = JSONObject(it).getJSONArray("data")

                for(i in 0 until documents.length()){
                    Document.todayDataType[documents.getJSONObject(i).getInt("docType")]?.add(Document(
                        documents.getJSONObject(i)
                    ))
                }
                setChatData()
            }catch (e:JSONException){
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"Can't connect to server.",Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    private fun setChatData(){
        setNotiData()
    }

    private fun setNotiData(){
        startActivity()
    }

    private fun checkPermission(): Boolean{
        return (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
    }
    private fun requestPermission(){
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if(grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity()
                } else {
                    permissionDialog()
                }
                return
            }
        }
    }
    private fun startActivity(){
        if(Setting.isFingerLock){
            val executor = ContextCompat.getMainExecutor(this)
            val biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(applicationContext,errString.toString(),Toast.LENGTH_SHORT).show()
                        this@StartingActivity.finish()
                        if(Setting.pin != null) {
                            intent = Intent(this@StartingActivity, PINActivity::class.java)
                            intent.putExtra("loginSuccess",loginSuccess)
                            startActivity(intent)
                            this@StartingActivity.finish()
                        }
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        startNextActivity()
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()

                biometricPrompt.authenticate(promptInfo)
        }else if(Setting.pin != null){
            intent = Intent(this@StartingActivity, PINActivity::class.java)
            intent.putExtra("loginSuccess",loginSuccess)
            startActivity(intent)
            finish()
        } else{
            startNextActivity()
        }
    }
    private fun startNextActivity(){
        if(loginSuccess){
            startActivity(Intent(this@StartingActivity, MainActivity::class.java))
        }else{
            startActivity(Intent(this@StartingActivity, LoginActivity::class.java))
        }
        finish()
    }


    private fun permissionDialog(){
        val localBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        localBuilder.setMessage(getString(R.string.permission_require))
            .setPositiveButton(getString(R.string.permission_go),
                DialogInterface.OnClickListener { _, _ ->
                    try {
                            val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:$packageName"))
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                        startActivity(intent)
                    }
                })
            .setNegativeButton(getString(R.string.permission_no)) { _, _ ->
                this.finish()
            }
            .create()
            .show()
    }
}