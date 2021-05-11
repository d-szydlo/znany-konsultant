package com.example.znanykonultant.consultant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R

class ConsultantAppointmentsListAdapter(private val appointments: List<String>)
    : RecyclerView.Adapter<ConsultantAppointmentsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.consultant_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = appointments[position]
    }

    override fun getItemCount() = appointments.size
}