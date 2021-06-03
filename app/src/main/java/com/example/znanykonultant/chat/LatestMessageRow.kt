package com.example.znanykonultant.chat

import android.view.View
import com.example.znanykonultant.R
import com.example.znanykonultant.databinding.RecyclerLatestMessageItemBinding
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.Human
import com.example.znanykonultant.entity.Messages
import com.example.znanykonultant.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.viewbinding.BindableItem
import java.text.SimpleDateFormat
import java.util.*

open class LatestMessageRow(private val message: Messages)
    : BindableItem<RecyclerLatestMessageItemBinding>() {
    var chatPartner : Human? = null

    override fun getLayout(): Int {
        return R.layout.recycler_latest_message_item
    }

    override fun bind(viewBinding: RecyclerLatestMessageItemBinding, position: Int) {
        viewBinding.lastMessageText.text = message.text
        viewBinding.lastMessageDate.text = getTimeFormatted(message.timestamp)
        bindSenderLogin(viewBinding)
    }

    private fun getTimeFormatted(time: Long): String {
        var dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
        if (isLessThanOneDayAgo(time))
            dateFormatter = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        return dateFormatter.format(Date(time))
    }

    private fun isLessThanOneDayAgo(time: Long) =
        time + DAY_IN_MILLIS > System.currentTimeMillis()

    private fun bindSenderLogin(viewBinding: RecyclerLatestMessageItemBinding) {
        val chatPartnerId: String = if (message.fromId == FirebaseAuth.getInstance().uid)
            message.toId
        else
            message.fromId

        bindUserLogin(chatPartnerId, viewBinding)
        if (isSenderLoginEmptyAfterBindingUser(viewBinding))
            bindConsultantLogin(chatPartnerId, viewBinding)
    }

    private fun bindUserLogin(
        chatPartnerId: String,
        viewBinding: RecyclerLatestMessageItemBinding
    ) {
        val userRef = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User? = snapshot.getValue(User::class.java)
                if (user != null) {
                    chatPartner = user
                    viewBinding.lastMessageSenderLogin.text = (chatPartner as User).getFullName()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun isSenderLoginEmptyAfterBindingUser(viewBinding: RecyclerLatestMessageItemBinding) =
        viewBinding.lastMessageSenderLogin.text == ""

    private fun bindConsultantLogin(
        chatPartnerId: String,
        viewBinding: RecyclerLatestMessageItemBinding
    ) {
        val consultantRef =
            FirebaseDatabase.getInstance().getReference("/consultants/$chatPartnerId")
        consultantRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val consultant: Consultant? = snapshot.getValue(Consultant::class.java)
                if (consultant != null) {
                    chatPartner = consultant
                    viewBinding.lastMessageSenderLogin.text =
                        (chatPartner as Consultant).getFullName()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun initializeViewBinding(view: View): RecyclerLatestMessageItemBinding {
        return RecyclerLatestMessageItemBinding.bind(view)
    }

    companion object {
        const val DAY_IN_MILLIS = 86400000
    }
}
