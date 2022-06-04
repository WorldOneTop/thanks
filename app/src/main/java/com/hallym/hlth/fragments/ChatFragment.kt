package com.hallym.hlth.fragments

//import com.hallym.hlth.function.ChatStorage
import android.animation.ObjectAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.hallym.hlth.ChatInActivity
import com.hallym.hlth.MainActivity
import com.hallym.hlth.MenteeManageData
import com.hallym.hlth.adapters.ChatAdapter
import com.hallym.hlth.databinding.FragmentChatBinding
import com.hallym.hlth.function.ChatController
import com.hallym.hlth.function.ChatDB
import com.hallym.hlth.function.Query
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var adapter: ChatAdapter
    private lateinit var listViewAdapter: ArrayAdapter<String>
    private lateinit var listItemName: ArrayList<String>
    private lateinit var listItemId: ArrayList<Int>
    private var addButtonIsOpen: Boolean = false

    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(intent.getStringExtra("category") == "send"){
                adapter.addChatting(
                    intent.getIntExtra("userId",0), intent.getStringExtra("date").toString(),intent.getStringExtra("content").toString(),intent.getStringExtra("name").toString()
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbarChat)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)


        initRecyclerView()
        initAddView()
    }

    private fun initRecyclerView() {
        adapter = ChatAdapter(requireContext())

        binding.rvChat.addItemDecoration(DividerItemDecoration(requireContext(),VERTICAL))
        binding.rvChat.adapter = adapter
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext())

        val intent = Intent( requireContext(),ChatInActivity::class.java )
        adapter.onClickListener = { userId,userName ->
            intent.putExtra("userId",userId)
            intent.putExtra("userName",userName)
            startActivity(intent)
        }
        adapter.onContextMenuItemClickListener = {
            ChatController(ChatDB(requireContext()).readableDatabase).deleteChatRoom(it.toString())
            adapter.deleteChatRoom(it)
        }
    }
    private fun initAddView(){
        listItemName = ArrayList()
        listItemId = ArrayList()

        listViewAdapter = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_list_item_1, listItemName)
        binding.chatAddList.adapter = listViewAdapter

        Query().getChatUserList{
            val data = it.getJSONArray("data")
            for(i in 0 until data.length()){
                listItemName.add(data.getJSONObject(i).getString("userId__name"))
                listItemId.add(data.getJSONObject(i).getInt("userId"))
            }
            CoroutineScope(Dispatchers.Main).launch {
                listViewAdapter.notifyDataSetChanged()
            }
        }

        ObjectAnimator.ofFloat(chatAddList, "translationY", 200f).start()

        binding.chatAddButton.setOnClickListener{
           if (addButtonIsOpen) {
               ObjectAnimator.ofFloat(chatAddList, "translationY", 200f).start()
               ObjectAnimator.ofFloat(chatAddList,"alpha",1f,0f).start()
               ObjectAnimator.ofFloat(it, View.ROTATION, 45f, 0f).start()
           } else {
               ObjectAnimator.ofFloat(chatAddList, "translationY", 0f).start()
               ObjectAnimator.ofFloat(chatAddList,"alpha",0f,1f).start()
               ObjectAnimator.ofFloat(it, View.ROTATION, 0f, 45f).start()
           }
           addButtonIsOpen = !addButtonIsOpen
       }

        binding.chatAddList.setOnItemClickListener{ _, _, position, _ ->
            val intent = Intent(requireContext(), ChatInActivity::class.java)
            intent.putExtra("userId", listItemId[position])
            intent.putExtra("userName", listItemName[position])
            startActivity(intent)
        }
    }

    override fun onStart() {
        adapter.setData(ChatController(ChatDB(requireContext()).readableDatabase).lastChatList())
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            mBroadcastReceiver, IntentFilter("Chatting"))
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mBroadcastReceiver)
        if(addButtonIsOpen){
            binding.chatAddButton.callOnClick()
        }
    }
}
