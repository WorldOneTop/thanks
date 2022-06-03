package com.hallym.hlth

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.ActivityDiaryBinding
import com.hallym.hlth.databinding.RowDiaryBinding
import com.hallym.hlth.function.DiaryController
import com.hallym.hlth.function.DiaryDB
import com.hallym.hlth.function.LoginStorage
import com.hallym.hlth.function.Query
import com.hallym.hlth.models.Document
import com.hallym.hlth.models.Term
import kotlinx.android.synthetic.main.dialog_add_diary.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryBinding
    private lateinit var adapter: DiaryAdapter
    private lateinit var dialog: Dialog
    private lateinit var diaryData: DiaryData
    private lateinit var progressDialog: Dialog
    private var isNewDiary:Boolean = true

    var onClickListener: ((data: DiaryData) -> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initDialog()
        initOnClick()
        setData()
    }
    private fun initView(){
        setSupportActionBar(binding.toolbarDiary)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = DiaryAdapter()

        binding.rvDiary.adapter = adapter
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.rvDiary.layoutManager =layoutManager
        binding.rvDiary.addItemDecoration(DividerItemDecoration(applicationContext,1))

    }
    private fun initDialog(){
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_diary)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.addDiaryClose.setOnClickListener{ dialog.dismiss() }
        diaryData = DiaryData()
        progressDialog = Dialog(this)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(ProgressBar(this))
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setOnCancelListener { this.finish() }
    }
    private fun initOnClick(){
        binding.toolbarDairyWrite.setOnClickListener{
            isNewDiary = true
            diaryData.date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().timeInMillis)
            diaryData.content = ""
            setDialogData(Document.todayDataType[0]!!,"")
            dialog.show()
        }
        adapter.onClickListener = { diaryData ->
            progressDialog.show()
            this.diaryData.id = diaryData.id
            this.diaryData.content = diaryData.content
            this.diaryData.date = diaryData.date

            isNewDiary = false
            Query().getDoc(LoginStorage.id.toString(),diaryData.date){
                val documents = JSONObject(it).getJSONArray("data")
                val thanksDoc:java.util.ArrayList<Document> = ArrayList()
                for(i in 0 until documents.length()){
                    if(documents.getJSONObject(i).getInt("docType") == 0){
                        thanksDoc.add(Document(documents.getJSONObject(i)))
                    }
                }
                CoroutineScope(Dispatchers.Main).launch {
                    setDialogData(thanksDoc,diaryData.content)
                    dialog.show()
                    progressDialog.dismiss()
                }
            }
        }
        dialog.addDiarySubmit.setOnClickListener{
            diaryData.content = dialog.addDiaryEdit.text.toString()
            if(isNewDiary){
                adapter.addData(DiaryData(
                    DiaryController(DiaryDB(applicationContext).writableDatabase).addDiary(diaryData.content, diaryData.date).toInt(),
                    diaryData.date,
                    diaryData.content
                ))
            }else{
                DiaryController(DiaryDB(applicationContext).writableDatabase).changeDiary(diaryData.id, diaryData.content)
                adapter.changeData(diaryData.id, diaryData.content)
            }

            dialog.dismiss()
        }
    }
    private fun setData(){
            adapter.setData(
                DiaryController(DiaryDB(applicationContext).writableDatabase).selectDiary()
            )
    }
    private fun setDialogData(documents:java.util.ArrayList<Document>, edit:String){
        val itemList:ArrayList<String> = ArrayList()
        for(doc in documents){
            itemList.add(doc.content)
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,itemList)
        dialog.addDiaryThanks.adapter = adapter
        dialog.addDiaryThanks.visibility = if(itemList.isEmpty()) View.GONE else View.VISIBLE

        dialog.addDiaryEdit.setText(edit)
        dialog.addDiaryEdit.setSelection(dialog.addDiaryEdit.length())
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }
}

class DiaryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data: ArrayList<DiaryData> = arrayListOf()
    var onClickListener: ((data: DiaryData) -> Unit)? = null

    fun setData(data:ArrayList<DiaryData>){
        val prev = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size)
    }
    fun addData(data:DiaryData){
        this.data.add(data)
        notifyItemInserted(this.data.size-1)
    }
    fun changeData(id:Int, content:String){
        var index:Int = -1
        for(i in 0 until data.size){
            if(data[i].id == id){
                index = i
                break
            }
        }

        this.data[index].content = content
        notifyItemChanged(index)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DiaryViewHolder(
            RowDiaryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DiaryViewHolder).bind(data[position])
        holder.onClickListener = onClickListener
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
class DiaryViewHolder(private val binding: RowDiaryBinding): RecyclerView.ViewHolder(binding.root) {
    var onClickListener: ((data: DiaryData) -> Unit)? = null

    fun bind(data:DiaryData){
        binding.rowDiaryDate.text = data.date
        binding.rowDiaryContent.text = data.content
        binding.root.setOnClickListener{ onClickListener?.let { it(data) }}
    }
}

data class DiaryData(
    var id:Int = -1,
    var date:String = "",
    var content:String = ""
)