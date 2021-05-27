package com.example.znanykonultant.user.appointments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.tools.DateTimeConverter
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class AppointmentsAdapter(private var data: List<Appointments>, private var ids : List<String>,
                          private val listener : OnItemClickListener)
    : RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(appointment: Appointments, id : String)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val consultant: TextView
        val client : TextView
        val place: TextView
        val time : TextView


        init {
            consultant = view.findViewById(R.id.appointmentsConsultant)
            place = view.findViewById(R.id.appointmentsPlace)
            time = view.findViewById(R.id.appointmentsDate)
            client = view.findViewById(R.id.appointmentsClient)
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    listener.onItemClick(data[adapterPosition], ids[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /* Create new view */
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_appointments_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Todo UID convertion
        viewHolder.consultant.text = data[position].consultantLogin
        viewHolder.place.text = data[position].place
        viewHolder.client.text = data[position].personLogin
        viewHolder.time.text = DateTimeConverter(data[position].timestamp).convert()
    }

    override fun getItemCount() = data.size

    fun updateData(data : List<Appointments>, appointmentIds : List<String>) {
        this.data = data
        ids = appointmentIds
        Log.i("firebase", data.toString())
        notifyDataSetChanged()
    }

}