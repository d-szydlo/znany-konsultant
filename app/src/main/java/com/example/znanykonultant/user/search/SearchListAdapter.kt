package com.example.znanykonultant.user.search

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Consultant
import java.util.*
import kotlin.Double.Companion.MAX_VALUE
import kotlin.Double.Companion.MIN_VALUE

class SearchListAdapter(
    private var data: MutableList<Consultant>,
    private var allData: MutableList<Consultant>,
    private var listener: SearchResultClickListener
    ) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    var nameFilter : String = ""
    var cityFilter : String = ""
    var priceMinFilter : Double = SearchFragment.PRICE_MIN_DEFAULT
    var priceMaxFilter : Double = SearchFragment.PRICE_MAX_DEFAULT
    var morningFilter : Boolean = false
    var afternoonFilter : Boolean = false
    var eveningFilter : Boolean = false
    var catITFilter : Boolean = false
    var catBusinessFilter : Boolean = false
    var catFinanceFilter : Boolean = false
    var catMarketingFilter : Boolean = false

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val searchItem : ConstraintLayout
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
            searchItem = view.findViewById(R.id.searchItem)
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

        holder.searchItem.setOnClickListener {
            listener.onSearchResultClick(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun sortItems(sortOption : Int){
        when (sortOption) {
            1 -> {
                //
            }
            2 -> {
                data.sortWith { x, y -> (getMinPrice(x) - getMinPrice(y)).toInt() }
            }
            3 -> {
                data.sortWith { x, y -> (getMinPrice(y) - getMinPrice(x)).toInt() }
            }
            4 -> {
                data.sortByDescending { it.averageRating }
            }
        }
        notifyDataSetChanged()
    }

    fun applyFilters(){
        data.clear()
        data.addAll(allData)

        if (nameFilter != "")
            data.removeAll { !(it.name + " " + it.surname).contains(nameFilter) }

        if (cityFilter != "")
            data.removeAll { it.city != cityFilter }

        if (catITFilter || catMarketingFilter || catFinanceFilter || catBusinessFilter)
            data.removeAll { !hasCategory(it) }

        data.removeAll { getMinPrice(it) < priceMinFilter }
        data.removeAll { getMaxPrice(it) > priceMaxFilter }

        notifyDataSetChanged()
    }

    private fun hasCategory(c : Consultant) : Boolean {
        if (catITFilter && c.category.containsKey("IT")) return true
        if (catFinanceFilter && c.category.containsKey("finanse i rachunkowość")) return true
        if (catBusinessFilter && c.category.containsKey("biznes")) return true
        if (catMarketingFilter && c.category.containsKey("marketing")) return true
        return false
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