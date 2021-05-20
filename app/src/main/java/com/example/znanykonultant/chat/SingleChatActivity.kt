package com.example.znanykonultant.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.databinding.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem

class SingleChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_chat)

        setInterlocutor()
        loadRecycler()
    }

    private fun loadRecycler() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        val chatsRecycler = findViewById<RecyclerView>(R.id.singleChatRecyclerView)
        addTempData(adapter)
        chatsRecycler.adapter = adapter
        chatsRecycler.layoutManager = LinearLayoutManager(this)
    }

    private fun addTempData(adapter: GroupAdapter<GroupieViewHolder>) {
        adapter.add(ChatFromItem("Hi :D"))
        adapter.add(ChatToItem("Hi :P"))
        adapter.add(ChatFromItem("I'm going to write long message to test this view"))
        adapter.add(ChatToItem("Looks like it's working"))
        adapter.add(ChatFromItem("Yep, it's working"))
    }

    private fun setInterlocutor() {
        val interlocutor = intent.getStringExtra(ChatsFragment.INTERLOCUTOR_KEY)
        supportActionBar?.title = interlocutor
    }
}

class ChatFromItem(private val text: String) : BindableItem<RecyclerMessageFromItemBinding>() {
    override fun getLayout(): Int {
        return R.layout.recycler_message_from_item
    }

    override fun bind(viewBinding: RecyclerMessageFromItemBinding, position: Int) {
        viewBinding.messageFrom.text = text
    }

    override fun initializeViewBinding(view: View): RecyclerMessageFromItemBinding {
        return RecyclerMessageFromItemBinding.bind(view)
    }
}

class ChatToItem(private val text: String) : BindableItem<RecyclerMessageToItemBinding>() {
    override fun getLayout(): Int {
        return R.layout.recycler_message_to_item
    }

    override fun bind(viewBinding: RecyclerMessageToItemBinding, position: Int) {
        viewBinding.messageTo.text = text
    }

    override fun initializeViewBinding(view: View): RecyclerMessageToItemBinding {
        return RecyclerMessageToItemBinding.bind(view)
    }
}
