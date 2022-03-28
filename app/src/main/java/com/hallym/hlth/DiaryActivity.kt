package com.hallym.hlth

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
        binding.rvDiary.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvDiary.addItemDecoration(DividerItemDecoration(applicationContext,1))


    }
    private fun initDialog(){
        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_diary)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.addDiaryClose.setOnClickListener{ dialog.dismiss() }
        diaryData = DiaryData(
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().timeInMillis),
            ""
        )
        progressDialog = Dialog(this)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(ProgressBar(this))
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setOnCancelListener { this.finish() }
    }
    private fun initOnClick(){
        binding.toolbarDairyWrite.setOnClickListener{
            diaryData.date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().timeInMillis)
            setDialgData(Document.todayDataType[0]!!,"")
            dialog.show()
        }
        adapter.onClickListener = { diaryData ->
            progressDialog.show()
            this.diaryData = diaryData
            Query().getDoc(LoginStorage.id.toString(),diaryData.date){
                val documents = JSONObject(it).getJSONArray("data")
                val thanksDoc:java.util.ArrayList<Document> = ArrayList()
                for(i in 0 until documents.length()){
                    if(documents.getJSONObject(i).getInt("docType") == 0){
                        thanksDoc.add(Document(documents.getJSONObject(i)))
                    }
                }
                setDialgData(thanksDoc,diaryData.content)
                CoroutineScope(Dispatchers.Main).launch {
                    dialog.show()
                    progressDialog.dismiss()
                }
            }
        }
        dialog.addDiarySubmit.setOnClickListener{
            diaryData.content = dialog.addDiaryEdit.text.toString()
            Toast.makeText(applicationContext,diaryData.date+", "+diaryData.content,Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
    }
    private fun setData(){
        adapter.setData(arrayListOf(
            DiaryData("2022-02-02","크고 끝까지 못할 오아이스도 소담스러운 청춘 아니다. 뼈 이 청춘에서만 청춘의 거친 소금이라 수 풀이 석가는 것이다. 웅대한 심장은 얼마나 끝까지 두손을 있는 있는 때문이다. 인생의 하여도 품으며, 사람은 갑 보라. 이상, 그림자는 든 불어 때문이다. 동산에는 품고 가장 이것이다. 그들에게 때에, 속잎나고, 스며들어 아니더면, 대중을 실현에 우리 사람은 봄바람이다. 찬미를 현저하게 같이, 산야에 있는 위하여서. 굳세게 위하여 싸인 그것을 위하여 동산에는 대중을 피부가 끓는다."),
            DiaryData("2022-02-03","크고 끝까지 못할 오아이스도 소담스러운 청춘 아니다. 뼈 이 청춘에서만 청춘의 거친 소금이라 수 풀이 석가는 것이다. 웅대한 심장은 얼마나 끝까지 두손을 있는 있는 때문이다. 인생의 하여도 품으며, 사람은 갑 보라. 이상, 그림자는 든 불어 때문이다. 동산에는 품고 가장 이것이다. 그들에게 때에, 속잎나고, 스며들어 아니더면, 대중을 실현에 우리 사람은 봄바람이다. 찬미를 현저하게 같이, 산야에 있는 위하여서. 굳세게 위하여 싸인 그것을 위하여 동산에는 대중을 피부가 끓는다."),
            DiaryData("2022-03-21","크고 끝까지 못할 오아이스도 소담스러운 청춘 아니다. 뼈 이 청춘에서만 청춘의 거친 소금이라 수 풀이 석가는 것이다. 웅대한 심장은 얼마나 끝까지 두손을 있는 있는 때문이다. 인생의 하여도 품으며, 사람은 갑 보라. 이상, 그림자는 든 불어 때문이다. 동산에는 품고 가장 이것이다. 그들에게 때에, 속잎나고, 스며들어 아니더면, 대중을 실현에 우리 사람은 봄바람이다. 찬미를 현저하게 같이, 산야에 있는 위하여서. 굳세게 위하여 싸인 그것을 위하여 동산에는 대중을 피부가 끓는다."),
            ))
    }
    private fun setDialgData(documents:java.util.ArrayList<Document>, edit:String){
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
    var date:String,
    var content:String
)