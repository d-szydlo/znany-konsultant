package com.example.znanykonultant.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.znanykonultant.databinding.*
import com.example.znanykonultant.entity.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class SingleChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySingleChatBinding
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setInterlocutor()
        loadRecycler()
        listenForMessages()
    }

    private fun setInterlocutor() {
        val interlocutor = intent.getStringExtra(ChatsFragment.INTERLOCUTOR_KEY)
        supportActionBar?.title = interlocutor
    }

    private fun loadRecycler() {
        val chatsRecycler = binding.singleChatRecyclerView
        chatsRecycler.adapter = adapter
        chatsRecycler.layoutManager = LinearLayoutManager(this)
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = "VQf0TQ1CKIPydNG58KLKF9gDmSl2"
        val ref = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Messages::class.java) ?: return
                if (message.fromId == FirebaseAuth.getInstance().uid) {
                    adapter.add(ChatFromItem(message.text))
                } else {
                    adapter.add(ChatToItem(message.text))
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun sendMessage(view: View) {
        val text = binding.editText.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = "VQf0TQ1CKIPydNG58KLKF9gDmSl2"
        val reference = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/messages/$toId/$fromId").push()

        if (fromId == null) return

        val message = Messages(reference.key!!, text, fromId, toId, System.currentTimeMillis())
        reference.setValue(message).addOnSuccessListener {
            binding.singleChatRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }
        toReference.setValue(message)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId").push()
        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId").push()
        latestMessageRef.setValue(message)
        latestMessageToRef.setValue(message)

        binding.editText.text.clear()
    }
}
