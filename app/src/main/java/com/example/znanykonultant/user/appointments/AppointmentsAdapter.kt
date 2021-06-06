package com.example.znanykonultant.user.appointments

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.entity.User
import com.example.znanykonultant.tools.DateTimeConverter
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class AppointmentsAdapter(private var data: List<Appointments>,
                          private val listener : OnItemClickListener)
    : RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {

    private lateinit var view: View

    interface OnItemClickListener {
        fun onItemClick(appointment: Appointments)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val consultant: TextView
        val client : TextView
        val place: TextView
        val date : TextView
        val confirmed: TextView


        init {
            consultant = view.findViewById(R.id.appointmentsConsultant)
            place = view.findViewById(R.id.appointmentsPlace)
            date = view.findViewById(R.id.appointmentsDate)
            client = view.findViewById(R.id.appointmentsClient)
            confirmed = view.findViewById(R.id.appointmentsConfirmedText)
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    listener.onItemClick(data[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /* Create new view */
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_appointments_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(!data[position].confirmed){
            view.findViewById<ConstraintLayout>(R.id.appointsLayout).setBackgroundColor(
                // yellow
                Color.parseColor("#FFEB3B"))
            viewHolder.confirmed.text = "Nie potwierdzono wizyty"

        } else {
            view.findViewById<ConstraintLayout>(R.id.appointsLayout).setBackgroundColor(
                // blue
                Color.parseColor("#2196F3"))
            viewHolder.confirmed.text = "Wizyta potwierdzona"
        }

        viewHolder.consultant.text = data[position].consultant
        viewHolder.place.text = data[position].place
        viewHolder.client.text = data[position].person

        val dateStart = DateTimeConverter(data[position].timestampStart).splitConverted()
        val dateStop = DateTimeConverter(data[position].timestampStop).splitConverted()

        viewHolder.date.text = "${dateStart[0]} ${dateStart[1]} - ${dateStop[1]}"
    }

    override fun getItemCount() = data.size

    fun updateData(newData : List<Appointments>) {
        data = newData.sortedBy { it.timestampStart }
        notifyDataSetChanged()
    }

}