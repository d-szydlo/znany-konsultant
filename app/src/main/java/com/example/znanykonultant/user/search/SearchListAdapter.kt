package com.example.znanykonultant.user.search

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Consultant
import kotlin.Double.Companion.MAX_VALUE
import kotlin.Double.Companion.MIN_VALUE

class SearchListAdapter(private var data: MutableList<Consultant>) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    var nameFilter : String = ""
    var cityFilter : String = ""
    var priceMinFilter : Double = 0.0
    var priceMaxFilter : Double = 100.0
    var morningFilter : Boolean = false
    var afternoonFilter : Boolean = false
    var eveningFilter : Boolean = false
    var catITFilter : Boolean = false
    var catBusinessFilter : Boolean = false
    var catFinanceFilter : Boolean = false
    var catMarketingFilter : Boolean = false

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val consultantNameSurname: TextView
        val consultantCity: TextView
        val consultantCategory: TextView
        val consultantPhoto: ImageView
        val consultantRating : TextView
        val consultantPrice : TextView
        var view : View

        init {
            consultantNameSurname = view.findViewById(R.id.consultantNameSurname)
            consultantCity = view.findViewById(R.id.consultantCity)
            consultantCategory = view.findViewById(R.id.consultantCategory)
            consultantPhoto = view.findViewById(R.id.consultantPhoto)
            consultantRating = view.findViewById(R.id.consultantRating)
            consultantPrice = view.findViewById(R.id.consultantPrice)
            this.view = view
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item_view, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.consultantNameSurname.text = data[position].name + " " + data[position].surname
        holder.consultantCity.text = data[position].city
        holder.consultantRating.text = data[position].averageRating.toString()
        var consultantCat = ""
        for ((key, _) in data[position].category){
            consultantCat += "$key, "
        }
        holder.consultantCategory.text = consultantCat.dropLast(2)
        if (data[position].consultantService.isNotEmpty()){
            holder.consultantPrice.text = getMinPrice(data[position]).toString() + " PLN - "+ getMaxPrice(data[position]).toString() + " PLN"
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun sortItems(sortOption : Int){
        when (sortOption) {
            4 -> {
                data.sortByDescending { it.averageRating }
            }
        }
        notifyDataSetChanged()
    }

    fun applyFilters(){

        if (nameFilter != ""){
            data = data.filter { (it.name + " " + it.surname) == nameFilter } as MutableList<Consultant>
        }

        if (cityFilter != ""){
            data = data.filter { it.city == cityFilter } as MutableList<Consultant>
        }

        if (catBusinessFilter){
            data = data.filter { it.category.containsKey("biznes") } as MutableList<Consultant>
        }

        if (catFinanceFilter){
            data = data.filter { it.category.containsKey("finanse i rachunkowość") } as MutableList<Consultant>
        }

        if (catITFilter){
            data = data.filter { it.category.containsKey("IT")} as MutableList<Consultant>
        }

        if (catMarketingFilter){
            data = data.filter { it.category.containsKey("marketing") } as MutableList<Consultant>
        }

        data = data.filter { x -> getMinPrice(x) >= priceMinFilter } as MutableList<Consultant>
        data = data.filter { x -> getMaxPrice(x) <= priceMaxFilter } as MutableList<Consultant>

        notifyDataSetChanged()
    }

    fun setData(data: MutableList<Consultant>){
        this.data = data
        applyFilters()
    }

    private fun getMinPrice(c : Consultant) : Double {
        var curMin = MAX_VALUE
        for ((_, value) in c.consultantService){
            if (value.cost < curMin){
                curMin = value.cost
            }
        }
        return curMin
    }

    private fun getMaxPrice(c : Consultant) : Double {
        var curMax = 0.0
        for ((_, value) in c.consultantService){
            if (value.cost > curMax){
                curMax = value.cost
            }
        }
        return curMax
    }

}