package com.example.znanykonultant.chat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Chats
import java.sql.Timestamp

class ChatsFragment : Fragment(), ChatListAdapter.OnChatClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)
        loadRecycler(view)
        return view
    }

    private fun loadRecycler(view: View) {
        val chatsRecycler = view.findViewById<RecyclerView>(R.id.chats_recycler)
        chatsRecycler.layoutManager = LinearLayoutManager(activity)
        chatsRecycler.adapter = ChatListAdapter(genTempData(), this)
        chatsRecycler.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    private fun genTempData(): List<Chats> {
        return listOf(
            Chats(
                "First message ever",
                "Unknown",
                Timestamp(System.currentTimeMillis() - 100000000000000)),
            Chats(
                "Once upon a time",
                "Storyteller",
                Timestamp(System.currentTimeMillis() - 100000000000)),
            Chats(
                "I'm writing a long message to check how my fragment works",
                "Tester",
                Timestamp(System.currentTimeMillis() - 123456789)),
            Chats(
                "It's alpha version",
                "Me",
                Timestamp(System.currentTimeMillis())),
            Chats(
                "New will be released in the future",
                "Me",
                Timestamp(System.currentTimeMillis()))
        )
    }

    override fun onChatClick(position: Int) {
        val intent = Intent(activity, SingleChatActivity::class.java)
        intent.putExtra("message", "Welcome to chat activity!")
        startActivity(intent)
    }
}
