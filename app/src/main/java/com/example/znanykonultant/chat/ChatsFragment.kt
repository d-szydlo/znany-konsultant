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
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.Messages
import com.example.znanykonultant.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatsFragment : Fragment() {
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val latestMessagesMap = HashMap<String, Messages>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)
        loadRecycler(view)
        listenForLatestMessages()
        return view
    }

    private fun loadRecycler(view: View) {
        val chatsRecycler = view.findViewById<RecyclerView>(R.id.chats_recycler)
        chatsRecycler.adapter = adapter
        chatsRecycler.layoutManager = LinearLayoutManager(activity)
        chatsRecycler.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(activity, SingleChatActivity::class.java)
            val row = item as LatestMessageRow

            if (row.chatPartner is Consultant) {
                val consultant = row.chatPartner as Consultant
                intent.putExtra(NAME_KEY, "${consultant.name} ${consultant.surname}")
                intent.putExtra(UID_KEY, consultant.uid)
            } else if (row.chatPartner is User) {
                val user = row.chatPartner as User
                intent.putExtra(NAME_KEY, "${user.name} ${user.surname}")
                intent.putExtra(UID_KEY, user.uid)
            }

            startActivity(intent)
        }
    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Messages::class.java) ?: return
                latestMessagesMap[snapshot.key!!] = message
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Messages::class.java) ?: return
                latestMessagesMap[snapshot.key!!] = message
                refreshRecyclerViewMessages()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.forEach { (_, message) -> adapter.add(LatestMessageRow(message)) }
    }

    companion object {
        const val NAME_KEY = "name_key"
        const val UID_KEY = "uid_key"
    }
}
