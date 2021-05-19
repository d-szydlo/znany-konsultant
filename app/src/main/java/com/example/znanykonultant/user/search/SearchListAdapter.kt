package com.example.znanykonultant.user.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Consultant

class SearchListAdapter(private var data: MutableList<Consultant>) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val consultantNameSurname: TextView
        val consultantCity: TextView
        val consultantCategory: TextView
        val consultantPhoto: ImageView
        val consultantRating : TextView
        var view : View

        init {
            consultantNameSurname = view.findViewById(R.id.consultantNameSurname)
            consultantCity = view.findViewById(R.id.consultantCity)
            consultantCategory = view.findViewById(R.id.consultantCategory)
            consultantPhoto = view.findViewById(R.id.consultantPhoto)
            consultantRating = view.findViewById(R.id.consultantRating)
            this.view = view
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.consultantNameSurname.text = data[position].name + " " + data[position].surname
        holder.consultantCity.text = data[position].city
        holder.consultantRating.text = "5"
        holder.consultantCategory.text = "IT"
    }

    override fun getItemCount(): Int {
        return data.size
    }

}