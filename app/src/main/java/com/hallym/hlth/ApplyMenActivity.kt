package com.hallym.hlth

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.ActivityApplyMenBinding
import com.hallym.hlth.databinding.RowApplyMenBinding
import com.hallym.hlth.databinding.RowChatBinding
import com.hallym.hlth.databinding.RowDailyBinding
import com.hallym.hlth.function.LoginStorage
import com.hallym.hlth.function.Query
import com.hallym.hlth.function.Setting
import com.hallym.hlth.models.Chatting
import com.hallym.hlth.models.Term
import com.hallym.hlth.viewholders.ChatViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

class ApplyMenActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityApplyMenBinding
    private lateinit var adapter: ApplyMenAdapter
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplyMenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setData()
    }

    private fun initView(){
        dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(ProgressBar(this))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnCancelListener { this.finish() }
        dialog.show()

        setSupportActionBar(binding.toolbarApplyMen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(intent.getBooleanExtra("isMentor",true)){
            supportActionBar?.setTitle(R.string.menu_apply_mentor)
        }else{
            supportActionBar?.setTitle(R.string.menu_apply_mentee)
        }
        adapter = ApplyMenAdapter(applicationContext,intent.getBooleanExtra("isMentor",true))
        adapter.onClickListener = { term, isMentor ->
            submitOnClick(term, isMentor)
        }

        binding.rvApplyMen.adapter = adapter
        binding.rvApplyMen.layoutManager = LinearLayoutManager(applicationContext)
    }
    private fun setData(){
        Query().getTerm(null){
            val terms = it.getJSONArray("data")
            val data = ArrayList<Term>()

            for( i in 0 until terms.length()){
                val activated = terms.getJSONObject(i).getString("activated")
                data.add(Term(
                    terms.getJSONObject(i).getInt("id"),
                    terms.getJSONObject(i).getString("startDate"),
                    terms.getJSONObject(i).getString("endDate"),
                    if(activated == "null") null else activated.toBoolean(),
                    this
                ))
            }
            CoroutineScope(Main).launch {
                adapter.setData(data)
                dialog.dismiss()
            }
        }
    }
    private fun submitOnClick(term: Term, isMentor: Boolean){
        when(LoginStorage.status){
            1-> {}
            2 -> {Toast.makeText(this,getString(R.string.menu_applyMentee_already),Toast.LENGTH_LONG).show(); return}
            3 -> {Toast.makeText(this,getString(R.string.menu_applyMentor_already),Toast.LENGTH_LONG).show(); return}
            else -> {Toast.makeText(this,getString(R.string.menu_applyMen_already),Toast.LENGTH_LONG).show(); return}
        }

        AlertDialog.Builder(this)
            .setTitle(
                if(isMentor) getString(R.string.menu_apply_mentor)
                else getString(R.string.menu_apply_mentee)
            )
            .setMessage(getString(R.string.apply_men_sure))
            .setPositiveButton(getString(R.string.OK)) { _: DialogInterface, _: Int ->
                val loadingDialog = Dialog(this)
                loadingDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                loadingDialog.setContentView(ProgressBar(this))
                loadingDialog.setCanceledOnTouchOutside(false)
                loadingDialog.setOnCancelListener { this.finish() }
                loadingDialog.show()
                Query().applyMentoring(term.id, if(isMentor) 1 else 0) {
                    CoroutineScope(Main).launch{
                        if(it.getString("status") == "OK"){
                            LoginStorage.status = if(isMentor) 2 else 3
                            Toast.makeText(applicationContext, getString(R.string.menu_manageMen_success),Toast.LENGTH_LONG).show()
                            finish()
                        }else if(it.getString("status") == "user have never been mentee"){
                            Toast.makeText(applicationContext, getString(R.string.menu_applyMentor_noMentee),Toast.LENGTH_LONG).show()
                        }
                        loadingDialog.dismiss()
                    }
                }
            }
            .setNegativeButton(getString(R.string.CANCEL)){ _, _ ->
            }
            .create()
            .show()


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}


class ApplyMenAdapter(private val context: Context, var isMentor: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: ArrayList<Term> = arrayListOf()
    var onClickListener: ((data: Term, isMentor: Boolean) -> Unit)? = null

    fun setData(data: ArrayList<Term>) {
        val prev = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size+1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ApplyMenViewHolder(
            RowApplyMenBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ApplyMenViewHolder).bind(data[position], isMentor)
        holder.onClickListener = onClickListener
    }

    override fun getItemCount(): Int {
        return data.size
    }
}


class ApplyMenViewHolder(private val binding: RowApplyMenBinding) : RecyclerView.ViewHolder(binding.root) {
    var onClickListener: ((data: Term, isMentor: Boolean) -> Unit)? = null

    fun bind(data: Term, isMentor: Boolean){
        binding.applyMenId.text = data.id.toString()+"???"
        binding.applyMenStatus.text = data.activatedStr
        binding.applyMenStartDate.text = data.startData
        binding.applyMenEndDate.text = data.endData

        if(data.activated != false)
            binding.applyMenSubmit.isEnabled = false
        binding.applyMenSubmit.setOnClickListener{ onClickListener?.let { it(data,isMentor) }}
    }
}