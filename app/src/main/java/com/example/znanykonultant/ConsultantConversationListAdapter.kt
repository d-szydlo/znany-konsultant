package com.example.znanykonultant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ConsultantConversationListAdapter(private val conversations: List<String>)
    : RecyclerView.Adapter<ConsultantConversationListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : ConsultantConversationListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.consultant_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConsultantConversationListAdapter.ViewHolder, position: Int) {
        holder.textView.text = conversations[position]
    }

    override fun getItemCount() = conversations.size
}