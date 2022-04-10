package com.hallym.hlth

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.ActivityMenteeManageBinding
import com.hallym.hlth.databinding.RowMenteesBinding
import com.hallym.hlth.function.Query
import com.hallym.hlth.models.Document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class MenteeManageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMenteeManageBinding
    private lateinit var adapter:MenteeManageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenteeManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerForContextMenu(binding.rvMenteeManage)

        initView()
        setData()

    }


    private fun initView(){
        setSupportActionBar(binding.toolbarMenteeManage)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        adapter = MenteeManageAdapter()

        binding.rvMenteeManage.layoutManager = GridLayoutManager(applicationContext, 2)
        binding.rvMenteeManage.adapter = adapter

        adapter.onClickListener = { id,name,term ->
            val intent = Intent(this,MenteeInfoActivity::class.java)
            intent.putExtra("userId",id)
            intent.putExtra("name",name)
            intent.putExtra("term",term)
            startActivity(intent)

        }
        adapter.sendChatListener = { id,name ->
            val intent = Intent(applicationContext, ChatInActivity::class.java)
            intent.putExtra("userId",id)
            intent.putExtra("userName",name)
            startActivity(intent)
        }

    }
    private fun setData(){
        val mentees = ArrayList<MenteeManageData>()

        Query().getMenteesDoc{
            val data = it.getJSONArray("data")

            for(i in 0 until data.length()){
                mentees.add(MenteeManageData(
                    data.getJSONObject(i).getInt("userId"),
                    data.getJSONObject(i).getString("userId__name"),
                    data.getJSONObject(i).getInt("term"),
                    data.getJSONObject(i).getInt("thanks"),
                    data.getJSONObject(i).getInt("kind"),
                    data.getJSONObject(i).getInt("save"),
                    data.getJSONObject(i).getInt("book"),
                ))
            }
            CoroutineScope(Dispatchers.Main).launch {
                adapter.setData(mentees)
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
class MenteeManageAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data: ArrayList<MenteeManageData> = arrayListOf()
    var onClickListener: ((index: Int,name:String,term:Int) -> Unit)? = null
    lateinit var sendChatListener:(userId:Int,userName:String) -> Unit


    fun setData(data:ArrayList<MenteeManageData>){
        val prev = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(0, prev)

        this.data.addAll(data)
        notifyItemRangeInserted(0, data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MenteeManageViewHolder(
            RowMenteesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MenteeManageViewHolder).bind(data[position])
        holder.onClickListener = onClickListener
        holder.sendChatListener = sendChatListener
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
class MenteeManageViewHolder(private val binding:RowMenteesBinding) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
    var onClickListener: ((index: Int,name:String,term:Int) -> Unit)? = null
    lateinit var sendChatListener:(userId:Int,userName:String) -> Unit

    private var userId = 0
    private var userName = ""
    init {
        itemView.setOnCreateContextMenuListener(this)
    }
    fun bind(data:MenteeManageData){
        userId = data.id
        userName = data.name
        binding.menteesName.text = data.name
        binding.menteesIcon.text = data.name[0].toString()

        binding.menteesThanks.text = data.thanks.toString()
        binding.menteesKind.text = data.kind.toString()
        binding.menteesSave.text = data.save.toString()
        binding.menteesBook.text = data.book.toString()

        binding.root.setOnClickListener{ onClickListener?.let { it(data.id,data.name,data.term) }}
    }
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.add(0, 0, 0, "쪽지 보내기")?.setOnMenuItemClickListener {sendChatListener(userId,userName)
                true
        }
    }
}
data class MenteeManageData(
    var id:Int,
    var name:String,
    var term:Int,
    var thanks:Int,
    var kind:Int,
    var save:Int,
    var book:Int,
)