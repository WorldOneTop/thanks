package com.hallym.hlth

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hallym.hlth.databinding.ActivityMenteeInfoBinding
import com.hallym.hlth.function.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject


class MenteeInfoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMenteeInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenteeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setData()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        setSupportActionBar(binding.toolbarMenteeInfo)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val name:String? = intent.getStringExtra("name")
        binding.menteeInfoTitle.text = name
        binding.menteeInfoName.text = name
        binding.menteeInfoIcon.text = name?.get(0).toString()
        binding.menteeInfoTerm.text = intent.getIntExtra("term",0).toString() + binding.menteeInfoTerm.text

        binding.menteeInfoMsg.setOnClickListener{
            val sendIntent = Intent(applicationContext, ChatInActivity::class.java)
            sendIntent.putExtra("userId",intent.getIntExtra("userId",0))
            sendIntent.putExtra("userName",name)
            startActivity(sendIntent)
        }
    }
    private fun setData(){
        val thanks:ArrayList<String> = ArrayList()
        val thanksAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.simple_list_item_1,thanks)

        Query().getDoc(intent.getIntExtra("id",0).toString(),Query.now()){
            try {
                val documents = JSONObject(it).getJSONArray("data")


                CoroutineScope(Dispatchers.Main).launch{

                    for(i in 0 until documents.length()){
                        when(documents.getJSONObject(i).getInt("docType")){
                            0 ->{
                                thanks.add(documents.getJSONObject(i).getString("content"))
                            }
                            1 ->{
                                binding.menteeInfoKind.text = documents.getJSONObject(i).getString("content")
                            }
                            2 ->{
                                binding.menteeInfoSave.text = documents.getJSONObject(i).getString("content")
                            }
                            3 ->{
                                binding.menteeInfoBook.text = documents.getJSONObject(i).getString("content")
                            }
                        }
                    }
                    if(thanks.isEmpty()){
                        thanks.add(" ")
                    }
                    binding.menteeInfoThanks.adapter = thanksAdapter
                    setListViewHeightBasedOnChildren(binding.menteeInfoThanks)
                }
            }catch (e: JSONException){
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"Can't connect to server.", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListViewHeightBasedOnChildren(listView: ListView){
        val listAdapter = listView.adapter
        var totalHeight = 0
        for(i in 0 until listAdapter.count){
            val listItem = listAdapter.getView(i,null,listView)
            listItem.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
        }

}