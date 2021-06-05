package com.example.znanykonultant.appointments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.entity.WorkDays


class OccupiedTermsAdapter (private var data: List<WorkDays>)
    : RecyclerView.Adapter<OccupiedTermsAdapter.ViewHolder>() {

    private lateinit var view: View

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val startDate: TextView
        val stopDate : TextView

        init {
            startDate = view.findViewById(R.id.timeStart)
            stopDate = view.findViewById(R.id.timeStop)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /* Create new view */
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_appointments_dates, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.startDate.text = data[position].start
        viewHolder.stopDate.text = data[position].stop
    }

    override fun getItemCount() = data.size

    fun updateData(data : List<WorkDays>) {
        this.data = data
        notifyDataSetChanged()
    }


}
