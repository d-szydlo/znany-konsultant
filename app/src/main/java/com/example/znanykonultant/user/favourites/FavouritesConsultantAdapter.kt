package com.example.znanykonultant.user.favourites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Favourite

class FavouritesConsultantAdapter(var context: Context, var FavouritesList: ArrayList<Favourite>): RecyclerView.Adapter<FavouritesConsultantAdapter.FavouritesConsultantViewHolder>() {

    class FavouritesConsultantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var view = itemView

        init{
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesConsultantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.search_item_view, parent, false)
        return FavouritesConsultantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return FavouritesList.size
    }

    override fun onBindViewHolder(holder: FavouritesConsultantAdapter.FavouritesConsultantViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}