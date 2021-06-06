package com.example.znanykonultant.user.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Consultant
import com.squareup.picasso.Picasso

class FavouritesConsultantAdapter(private var data: List<Consultant>, private var ids: List<String>, private val listener : OnItemClickListener)
    : RecyclerView.Adapter<FavouritesConsultantAdapter.ViewHolder>()  {

    private lateinit var view: View
    var data2: List<Consultant> = data

    interface OnItemClickListener {
        fun onItemClick(consultant: Consultant, id : String)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val price: TextView
        val nameSurname : TextView
        val city: TextView
        val category : TextView
        val rating: TextView
        val photo : ImageView


        init {
            price = view.findViewById(R.id.consultantPrice)
            nameSurname = view.findViewById(R.id.consultantNameSurname)
            city = view.findViewById(R.id.consultantCity)
            category = view.findViewById(R.id.consultantCategory)
            rating = view.findViewById(R.id.consultantRating)
            photo = view.findViewById(R.id.consultantPhoto)
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    listener.onItemClick(data[adapterPosition], ids[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesConsultantAdapter.ViewHolder {
        view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_item_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: FavouritesConsultantAdapter.ViewHolder, position: Int) {

        viewHolder.nameSurname.text = data[position].name + data[position].surname
        viewHolder.city.text = data[position].city
        viewHolder.category.text = prepareCategoriesToDisplay(data[position].category).toString()
        viewHolder.rating.text = data[position].averageRating.toString()
        if (data[position].picture != ""){
            try {
                Picasso.get().load(data[position].picture).into(viewHolder.photo)
            }catch(e: IllegalArgumentException){}
        }
    }

    override fun getItemCount() = data.size

    fun updateData(data : List<Consultant>, consultantsIds : List<String>) {
        this.data = data
        ids = consultantsIds
        notifyDataSetChanged()
    }

    private fun prepareCategoriesToDisplay(categories: Map<String, Boolean>): String {
        var text: String = ""
        for ((c, b) in categories){
            text += c
            text += " "
        }
        return text
    }
}