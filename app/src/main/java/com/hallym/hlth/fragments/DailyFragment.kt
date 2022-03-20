package com.hallym.hlth.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.hallym.hlth.R
import com.hallym.hlth.adapters.DailyAdapter
import com.hallym.hlth.databinding.FragmentDailyBinding
import com.hallym.hlth.function.Query
import com.hallym.hlth.models.Document
import kotlinx.android.synthetic.main.dialog_add_doc.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


class DailyFragment : Fragment() {

    private lateinit var binding: FragmentDailyBinding
    private lateinit var adapter: DailyAdapter
    private lateinit var dialog: Dialog
    private var currentPosition = 0
    private var imagePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_doc)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = DailyFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTab()
        initDialog()
    }

    private fun initTab(){
        adapter = DailyAdapter(requireContext())
        adapter.onClickListener={ showDialog() }


        binding.tabbarDaily.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                changeTab(tab.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
        changeTab(currentPosition)
        binding.tabbarDaily.selectTab(binding.tabbarDaily.getTabAt(currentPosition))

        binding.rvDaily.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDaily.adapter = adapter

    }
    private fun changeTab(index: Int){
        currentPosition = index
        when(index){
            1 ->{
                binding.dailyTitle.text = getString(R.string.title_save)
                binding.dailySubTitle.text = getString(R.string.daily_save_text)
                adapter.setData(index)
            }
            2 ->{
                binding.dailyTitle.text = getString(R.string.title_kind)
                binding.dailySubTitle.text = getString(R.string.daily_kind_text)
                adapter.setData(index)
            }
            3 ->{
                binding.dailyTitle.text = getString(R.string.title_book)
                binding.dailySubTitle.text = getString(R.string.daily_book_text)
                adapter.setData(index)
            }
            else ->{
                binding.dailyTitle.text = getString(R.string.title_thanks)
                binding.dailySubTitle.text = getString(R.string.daily_thanks_text)
                adapter.setData(index)
            }
        }
    }
    private fun initDialog(){
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = MediaStore.Images.Media.CONTENT_TYPE
            data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val launcher = registerForActivityResult(StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK) {
                result.data?.data?.let {
                    imagePath = getRealPathFromURI(it)
                    if(imagePath == null){
                        Toast.makeText(requireContext(), getString(R.string.daily_image_load_fail), Toast.LENGTH_SHORT).show()
                    }else{
                        dialog.addDocImage.setImageURI(it)
                        dialog.addDocImage.setBackgroundColor(0)
                    }
                }
            }
        }

        dialog.addDocClose.setOnClickListener {
            closeDialog()
        }
        dialog.addDocSubmit.setOnClickListener {
            if(checkForm()){
                uploadDoc()
            }
        }
        dialog.addDocImage.setOnClickListener{
            launcher.launch(intent)
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = requireContext().contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }


    private fun checkForm(): Boolean{
        if(dialog.addDocEdit.text.isEmpty()){
            Toast.makeText(requireContext(),getString(R.string.daily_add_fail),Toast.LENGTH_SHORT).show()
            return false
        }
        if(currentPosition == 2 && imagePath == null){
            Toast.makeText(requireContext(),getString(R.string.daily_add_fail),Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun showDialog() {
        dialog.window?.setLayout(binding.root.width,binding.root.height)
        dialog.addDocImage.visibility = if(currentPosition != 2) View.GONE else View.VISIBLE
        dialog.show()
    }
    private fun uploadDoc(){
        Query().uploadDoc(dialog.addDocEdit.text.toString(),currentPosition,imagePath){
            Document.todayDataType[currentPosition]!!.add(Document( it.getJSONObject("data")))
            CoroutineScope(Main).launch {
                adapter.setData(currentPosition)
            }
        }
        closeDialog()

    }
    private fun closeDialog(){
        dialog.addDocEdit.setText("")
        imagePath?.let {
            imagePath = null
            dialog.addDocImage.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24)
            dialog.addDocImage.setBackgroundResource(R.drawable.bg_secondary_rounded)
        }
        dialog.dismiss()
    }

}
