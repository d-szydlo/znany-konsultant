package com.example.znanykonultant.consultant.profile

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.ConsultantService


class ConsultantServicesAdapter(var context: Context, var ServicesList: MutableList<ConsultantService>): RecyclerView.Adapter<ConsultantServicesAdapter.ConsultantServicesViewHolder>(){

    class ConsultantServicesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var consultant_service_name: TextView
        var consultant_service_desc: TextView
        var consultant_service_price: TextView
        var view = itemView

        init{
            consultant_service_name = itemView.findViewById(R.id.consultant_service_name)
            consultant_service_desc = itemView.findViewById(R.id.consultant_service_desc)
            consultant_service_price = itemView.findViewById(R.id.consultant_service_price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantServicesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.consultant_service_item, parent, false)
        return ConsultantServicesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ServicesList.size
    }

    override fun onBindViewHolder(holder: ConsultantServicesViewHolder, position: Int) {
        val currentService = ServicesList[position]

        holder.consultant_service_name.text = currentService.type
        holder.consultant_service_desc.text = currentService.description
        holder.consultant_service_price.text = currentService.cost.toString()

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.view.context, ConsultantServiceEdit::class.java)
            intent.putExtra("key", currentService.id)
            intent.putExtra("type", currentService.type)
            intent.putExtra("desc", currentService.description)
            intent.putExtra("price", currentService.cost)
            holder.view.context.startActivity(intent)
            true
        }
    }
}