package com.example.znanykonultant.consultant.services

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.ConsultantService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ConsultantServicesAdapter(var context: Context, var ServicesList: MutableList<ConsultantService>): RecyclerView.Adapter<ConsultantServicesAdapter.ConsultantServicesViewHolder>(){

    val consultantUid = FirebaseAuth.getInstance().uid
    val consultantRef = Firebase.database.getReference("consultants/$consultantUid/consultantService")
    lateinit var currentService : ConsultantService
    var dialogClickListener = DialogInterface.OnClickListener { _, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                delete()
            }
            DialogInterface.BUTTON_NEGATIVE -> {
            }
        }
    }

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
        currentService = ServicesList[position]

        holder.consultant_service_name.text = currentService.type
        holder.consultant_service_desc.text = currentService.description
        holder.consultant_service_price.text = currentService.cost.toString()

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.view.context, ConsultantServiceEdit::class.java)
            intent.putExtra("key", ServicesList[position].id)
            intent.putExtra("type", ServicesList[position].type)
            intent.putExtra("desc", ServicesList[position].description)
            intent.putExtra("price", ServicesList[position].cost)
            holder.view.context.startActivity(intent)
            true
        }

        holder.itemView.setOnLongClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setMessage("Na pewno chcesz usunąć tą usługę?").setPositiveButton("Tak", dialogClickListener)
                    .setNegativeButton("Nie", dialogClickListener).show()
            true
        }
    }

    private fun delete(){
        consultantRef.child(currentService.id).removeValue()
        notifyDataSetChanged()
    }
}