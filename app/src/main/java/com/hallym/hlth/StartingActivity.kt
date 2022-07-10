package com.hallym.hlth

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hallym.hlth.databinding.ActivityStartingBinding
import com.hallym.hlth.function.InitData
import com.hallym.hlth.function.LoginStorage
import com.hallym.hlth.function.Query
import com.hallym.hlth.function.Setting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject


class StartingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartingBinding
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if(checkPermission()) {
            checkVersion()
        }else{
            requestPermission()
        }
    }

    private fun checkVersion(){
        Query().checkVersion{
            CoroutineScope(Dispatchers.Main).launch{
                if(BuildConfig.VERSION_CODE != it) {
                    createUpdateDialog()
                }
                else {
                    initData()
                    startApp()
                }
            }
        }
    }
    private fun createUpdateDialog(){
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.error_update))
            .setPositiveButton("download") { _: DialogInterface, _: Int ->
                val appPackageName = packageName
                try {
                    startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=$appPackageName")))
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }
            }
            .setNegativeButton(getString(R.string.CANCEL)){ _, _ ->
                initData()
                startApp()
            }
            .setCancelable(false)
            .create()
            .show()
    }
    private fun initData(){
        LoginStorage(applicationContext).loadData()
        Setting(applicationContext).loadData()
    }
    private fun startApp(){
        if(Setting.isAutoLogin){
            Query().login(LoginStorage.id!!, LoginStorage.pw!!){
                try {
                    val status = JSONObject(it)
                    if(status.getString("status") == "OK"){
                        Query.CSRF = status.getString("CSRF")
                        LoginStorage.status = status.getInt("userStatus")
                        InitData(this){
                            startNextActivity(true)
                        }
                    }else{
                        startNextActivity(false)
                    }
                }catch (e:JSONException){
                    CoroutineScope(Dispatchers.Main).launch{
                        Toast.makeText(applicationContext,"Can't connect to server.",Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        }else{
            startNextActivity(false)
        }
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
                    startNextActivity(false)
                } else {
                    permissionDialog()
                }
                return
            }
        }
    }

    private fun startNextActivity(isLogined:Boolean){
        if(isLogined){
            val nextIntent = Intent(this@StartingActivity, MainActivity::class.java)
            nextIntent.putExtra("link", intent.getIntArrayExtra("link"))
            intent.removeExtra("link")
            startActivity(nextIntent)
        }else{
            startActivity(Intent(this@StartingActivity, LoginActivity::class.java))
        }
        finish()
    }

    private fun permissionDialog(){
        val localBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        localBuilder.setMessage(getString(R.string.permission_require))
            .setPositiveButton(getString(R.string.permission_go)
            ) { _, _ ->
                try {
                    val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.parse("package:$packageName"))
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                    startActivity(intent)
                }
            }
            .setNegativeButton(getString(R.string.permission_no)) { _, _ ->
                this.finish()
            }
            .create()
            .show()
    }
}