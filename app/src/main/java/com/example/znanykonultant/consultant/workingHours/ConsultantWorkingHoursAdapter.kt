package com.example.znanykonultant.consultant.workingHours

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.WorkDays

class ConsultantWorkingHoursAdapter(var context: Context, var workDaysList: MutableList<WorkDays>)
    : RecyclerView.Adapter<ConsultantWorkingHoursAdapter.ConsultantServicesViewHolder>() {

    class ConsultantServicesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val day : TextView = itemView.findViewById(R.id.day)
        val startHour : TextView = itemView.findViewById(R.id.start_hour)
        val endHour : TextView = itemView.findViewById(R.id.end_hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantServicesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.consultant_work_day_item, parent, false)
        return ConsultantServicesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConsultantServicesViewHolder, position: Int) {
        holder.day.text = workDaysList[position].day
        holder.startHour.text = workDaysList[position].start
        holder.endHour.text = workDaysList[position].stop
    }

    override fun getItemCount() = workDaysList.size
}
