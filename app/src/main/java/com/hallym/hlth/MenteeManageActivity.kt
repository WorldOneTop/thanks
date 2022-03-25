package com.hallym.hlth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hallym.hlth.databinding.ActivityMenteeManageBinding
import com.hallym.hlth.databinding.RowMenteesBinding
import com.hallym.hlth.models.Document


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

        adapter.onClickListener = {
            val intent = Intent(this,SettingActivity::class.java)
            intent.putExtra("id",it)
            startActivity(intent)

        }
    }
    private fun setData(){

        adapter.setData(arrayListOf(
            MenteeManageData(20185159,"홍길동",1,2,0,0),
            MenteeManageData(20185159,"고길동",0,2,2,0),
        ))
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
    var onClickListener: ((index: Int) -> Unit)? = null


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
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
class MenteeManageViewHolder(private val binding:RowMenteesBinding) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
    var onClickListener: ((index: Int) -> Unit)? = null

    init {
        itemView.setOnCreateContextMenuListener(this)
    }
    fun bind(data:MenteeManageData){
        binding.menteesName.text = data.name
        binding.menteesIcon.text = data.name[0].toString()

        binding.menteesThanks.text = data.thanks.toString()
        binding.menteesKind.text = data.kind.toString()
        binding.menteesSave.text = data.save.toString()
        binding.menteesBook.text = data.book.toString()

        binding.root.setOnClickListener{ onClickListener?.let { it(data.id) }}
    }
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.add(0, 0, 0, "쪽지 보내기")?.setOnMenuItemClickListener {

            true
        }
    }
}
data class MenteeManageData(
    var id:Int,
    var name:String,
    var thanks:Int,
    var kind:Int,
    var save:Int,
    var book:Int,
)