package com.example.znanykonultant.consultant.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R

class ConsultantChatsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_consultant_chats, container, false)
        loadRecycler(view)
        return view
    }

    private fun loadRecycler(view: View) {
        val chatsRecycler = view.findViewById<RecyclerView>(R.id.chats_recycler)
        chatsRecycler.layoutManager = LinearLayoutManager(activity)
        chatsRecycler.adapter = ConsultantChatListAdapter(genTempData())
    }

    private fun genTempData(): List<String> {
        return listOf("abc", "def", "xyz")
    }
}
