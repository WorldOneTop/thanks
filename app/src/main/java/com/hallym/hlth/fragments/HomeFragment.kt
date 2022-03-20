package com.hallym.hlth.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallym.hlth.MainActivity
import com.hallym.hlth.NotificationActivity
import com.hallym.hlth.R
import com.hallym.hlth.UserInfoActivity
import com.hallym.hlth.adapters.HomeAdapter
import com.hallym.hlth.adapters.HomeGoalsValueObject
import com.hallym.hlth.databinding.FragmentHomeBinding
import com.hallym.hlth.models.Document
import kotlinx.android.synthetic.main.dialog_add_doc.*
import kotlinx.android.synthetic.main.dialog_detail_doc.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter
    private lateinit var docDialog: Dialog
    private lateinit var docDialogLayout:LinearLayout.LayoutParams
    private lateinit var viewDate:String
    private lateinit var currentDate:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbarHome)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        viewDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().timeInMillis)
        currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().timeInMillis)

        if(viewDate == currentDate) {
            initRecyclerView()
        }
        initDialog()
    }

    private fun initRecyclerView() {
        adapter = HomeAdapter(requireContext())

        adapter.setData(
            arrayOf(
                "",
                HomeGoalsValueObject(
                    getString(R.string.title_thanks),
                    5,
                    Document.homeDataType[0]!!.size,
                    Document.homeDataType[0]!!
                ),
                HomeGoalsValueObject(
                    getString(R.string.title_save),
                    1,
                    Document.homeDataType[1]!!.size,
                    Document.homeDataType[1]!!
                ),
                HomeGoalsValueObject(
                    getString(R.string.title_kind),
                    1,
                    Document.homeDataType[2]!!.size,
                    Document.homeDataType[2]!!
                ),
                HomeGoalsValueObject(
                    getString(R.string.title_book),
                    1,
                    Document.homeDataType[3]!!.size,
                    Document.homeDataType[3]!!
                )
            )
        )

        adapter.onCardClickListener = { detailDocDialog(it)}

        adapter.onLinkClickListener = { url ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        binding.rvHome.adapter = adapter
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun initDialog(){
        docDialog = Dialog(requireContext())
        docDialog.setContentView(R.layout.dialog_detail_doc)
        docDialog.detailDocClose.setOnClickListener{ docDialog.dismiss() }

        docDialogLayout = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        docDialogLayout.topMargin = 40
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_notification -> {
                startActivity(
                    Intent(
                        requireContext(),
                        NotificationActivity::class.java
                    )
                )
            }

            R.id.action_account -> {
                startActivity(
                    Intent(
                        requireContext(),
                        UserInfoActivity::class.java
                    )
                )
            }

            else -> super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    private fun detailDocDialog(documents: ArrayList<Document>){
        if(documents.isEmpty())
            return
        docDialog.window?.setLayout(binding.root.width,binding.root.height)
        docDialog.detailDocRoot.removeAllViews()
        docDialog.detailDocTitle.text = documents[0].typeToStr()

        for(doc in documents){
            val tv = TextView(requireContext())
            tv.text = doc.content
            tv.setPadding(20)
            tv.layoutParams = docDialogLayout
            tv.setBackgroundResource(R.drawable.bg_secondary_rounded)
            docDialog.detailDocRoot.addView(tv)
        }
        docDialog.show()
    }
}