package com.hallym.hlth

import android.app.Dialog
import android.content.Context
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
import com.hallym.hlth.function.Query
import com.hallym.hlth.models.Chatting
import com.hallym.hlth.models.Term
import com.hallym.hlth.viewholders.ChatViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.json.JSONObject

class ApplyMenActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityApplyMenBinding
    private lateinit var adapter: ApplyMenAdapter
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_men)
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
            supportActionBar?.setTitle(R.string.apply_mentor_title)
        }else{
            supportActionBar?.setTitle(R.string.apply_mentee_title)
        }
        adapter = ApplyMenAdapter(applicationContext,intent.getBooleanExtra("isMentor",true))
        adapter.onClickListener = {term, isMentor ->
            Toast.makeText(applicationContext,"${term.id}기 ${if(isMentor) "멘토" else "멘티"} 신청",Toast.LENGTH_SHORT).show()
        }

        binding.rvApplyMen.adapter = adapter
        binding.rvApplyMen.layoutManager = LinearLayoutManager(applicationContext)
    }
    private fun setData(){
        Query().getTerm(null){
            val terms = it.getJSONArray("data")
            var data = ArrayList<Term>()

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
        binding.applyMenId.text = data.id.toString()+"기"
        binding.applyMenStatus.text = data.activatedStr
        binding.applyMenStartDate.text = data.startData
        binding.applyMenEndDate.text = data.endData

        if(data.activated != false)
            binding.applyMenSubmit.isEnabled = false
        binding.applyMenSubmit.setOnClickListener{ onClickListener?.let { it(data,isMentor) }}
    }
}