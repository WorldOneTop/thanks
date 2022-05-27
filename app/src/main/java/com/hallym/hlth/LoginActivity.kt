package com.hallym.hlth

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hallym.hlth.databinding.ActivityLoginBinding
import com.hallym.hlth.function.InitData
import com.hallym.hlth.function.Query
import com.hallym.hlth.function.Setting
import com.hallym.hlth.function.LoginStorage
import kotlinx.android.synthetic.main.dialog_login_help.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val pattern = Pattern.compile("20[0-9]{6}\$")
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDialog()
        initActv()
    }
    private fun initDialog(){
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_login_help)
        dialog.loginHelpChoice1.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://was1.hallym.ac.kr:8087/hlwc/mdi/Login.html")))
        }
        dialog.loginHelpChoice2.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hallym.ac.kr/hallym_univ/sub04/cP13/sCP4/tab1.html")))
        }
        dialog.loginHelpChoice3.setOnClickListener{
            dialog.dismiss()
        }

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
        binding.loginHelp.setOnClickListener{
            dialog.show()
        }
        binding.loginPrivacy.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hallym.ac.kr/hallym_univ/sub07/cP2/tab2")))
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
                    Query.CSRF = status.getString("CSRF")
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