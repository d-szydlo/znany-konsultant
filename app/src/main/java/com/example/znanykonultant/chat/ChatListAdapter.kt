package com.example.znanykonultant.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Chats
import java.text.SimpleDateFormat
import java.util.*

class ChatListAdapter(private val chats: List<Chats>, private val listener: OnChatClickListener)
    : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val lastMessageText: TextView = view.findViewById(R.id.lastMessageText)
        val lastMessageDate: TextView = view.findViewById(R.id.lastMessageDate)
        val senderLogin: TextView = view.findViewById(R.id.lastMessageSenderLogin)

        init { view.setOnClickListener(this) }

        override fun onClick(v: View?) {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                listener.onChatClick(bindingAdapterPosition)
            }
        }
    }

    interface OnChatClickListener {
        fun onChatClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_chat_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.senderLogin.text = chats[position].senderLogin
        holder.lastMessageText.text = chats[position].lastMessage
        holder.lastMessageDate.text = getTimeFormatted(chats[position].timestamp.time)
    }

    private fun getTimeFormatted(time: Long): String {
        var dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
        if (isLessThanOneDayAgo(time))
            dateFormatter = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        return dateFormatter.format(Date(time))
    }

    private fun isLessThanOneDayAgo(time: Long) =
        time + DAY_IN_MILLIS > System.currentTimeMillis()

    override fun getItemCount() = chats.size

    companion object {
        const val DAY_IN_MILLIS = 86400000
    }
}
