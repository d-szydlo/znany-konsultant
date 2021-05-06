package com.example.znanykonultant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ConsultantListAdapter(private val consultants: List<String>)
    : RecyclerView.Adapter<ConsultantListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantListAdapter.ViewHolder {
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.consultant_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConsultantListAdapter.ViewHolder, position: Int) {
        holder.textView.text = consultants[position]
    }

    override fun getItemCount() = consultants.size
}