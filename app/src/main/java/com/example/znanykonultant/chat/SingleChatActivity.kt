package com.example.znanykonultant.chat

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.znanykonultant.databinding.ActivitySingleChatBinding
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
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private var chatPartnerId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setChatPartner()
        loadRecycler()
        listenForMessages()
    }

    private fun setChatPartner() {
        chatPartnerId = intent.getStringExtra(ChatsFragment.UID_KEY)
        val chatPartnerName = intent.getStringExtra(ChatsFragment.NAME_KEY)
        supportActionBar?.title = chatPartnerName
    }

    private fun loadRecycler() {
        val chatsRecycler = binding.singleChatRecyclerView
        chatsRecycler.adapter = adapter
        chatsRecycler.layoutManager = LinearLayoutManager(this)
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$chatPartnerId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Messages::class.java) ?: return
                if (message.fromId == FirebaseAuth.getInstance().uid) {
                    adapter.add(ChatFromItem(message.text))
                } else {
                    adapter.add(ChatToItem(message.text))
                }
                binding.singleChatRecyclerView.scrollToPosition(adapter.itemCount - 1)
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
        if (text.isNotEmpty()) {
            val fromId = FirebaseAuth.getInstance().uid ?: return
            saveMessageToDatabase(fromId, text)
            binding.editText.text.clear()
        }
    }

    private fun saveMessageToDatabase(fromId: String, text: String) {
        val reference =
            FirebaseDatabase.getInstance().getReference("/messages/$fromId/$chatPartnerId").push()
        val toReference =
            FirebaseDatabase.getInstance().getReference("/messages/$chatPartnerId/$fromId").push()

        val message =
            Messages(reference.key!!, text, fromId, chatPartnerId!!, System.currentTimeMillis())
        reference.setValue(message)
        toReference.setValue(message)

        saveLatestMessageToDatabase(fromId, message)
    }

    private fun saveLatestMessageToDatabase(fromId: String, message: Messages) {
        val latestMessageRef =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$chatPartnerId")
        val latestMessageToRef =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$chatPartnerId/$fromId")
        latestMessageRef.setValue(message)
        latestMessageToRef.setValue(message)
    }
}
