package com.hallym.hlth

import android.R
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.hallym.hlth.databinding.ActivityMenteeInfoBinding


class MenteeInfoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMenteeInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenteeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setData()
    }
    private fun initView(){
        setSupportActionBar(binding.toolbarMenteeInfo)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val name:String? = intent.getStringExtra("name")
        binding.menteeInfoTitle.text = name
        binding.menteeInfoName.text = name
        binding.menteeInfoIcon.text = name?.get(0).toString()
//        binding.menteeInfoTerm.text = binding.menteeInfoTerm.text

        binding.menteeInfoMsg.setOnClickListener{
            intent.getIntExtra("id",0)
        }
    }
    private fun setData(){
//        Query().getDoc(date){
//            try {
//                val documents = JSONObject(it).getJSONArray("data")
//
//                for(i in 0 until documents.length()){
//                    Document.todayDataType[documents.getJSONObject(i).getInt("docType")]?.add(
//                        Document(
//                            documents.getJSONObject(i)
//                        )
//                    )
//                }
//                setChatData()
//            }catch (e: JSONException){
//                CoroutineScope(Dispatchers.Main).launch{
//                    Toast.makeText(applicationContext,"Can't connect to server.", Toast.LENGTH_LONG).show()
//                    finish()
//                }
//            }
//        }

        val thanks:ArrayList<String> = ArrayList()
        var kind:String = "ArrayList()"
        var save:String = "ArrayList()\nArrayList()\nArrayList()\nArrayList()\nArrayList()\nArrayList()\nArrayList()"
        var book:String = "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()\n" +
                "ArrayList()"

        thanks.add("\"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +\n" +
                "                \"ArrayList()\\n\" +")
        thanks.add("asvasdvasf")
        thanks.add("asvasdvasf")
        thanks.add("asvasdvasf")
        thanks.add("asvasdvasf")
        thanks.add("asvasdvasf")
        thanks.add("asvasdvasf")

        val thanksAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.simple_list_item_1,thanks)
        binding.menteeInfoThanks.adapter = thanksAdapter
        setListViewHeightBasedOnChildren(binding.menteeInfoThanks)
        binding.menteeInfoKind.text = null
        binding.menteeInfoSave.text = save
        binding.menteeInfoBook.text = book
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListViewHeightBasedOnChildren(listView: ListView){
        val listAdapter = listView.getAdapter()
        var totalHeight = 0
        for(i in 0 until listAdapter.count){
            val listItem = listAdapter.getView(i,null,listView)
            listItem.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += listItem.measuredHeight;
        }

        val params = listView.layoutParams;
        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1));
        listView.layoutParams = params;
        listView.requestLayout();
        }

}