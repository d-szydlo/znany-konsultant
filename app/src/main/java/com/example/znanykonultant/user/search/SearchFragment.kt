package com.example.znanykonultant.user.search

import android.content.Intent
import android.icu.util.BuddhistCalendar
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.dao.ConsultantDAO
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.user.UserMainPageActivity
import com.example.znanykonultant.user.consultant.profile.UserConsultantProfileActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface SearchResultClickListener {
    fun onSearchResultClick(position: Int)
}

class SearchFragment : Fragment(), SearchResultClickListener {

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter: SearchListAdapter
    private val database = Firebase.database
    private val consultantRef = database.getReference("consultants")
    private var consultants : MutableList<Consultant> = mutableListOf()
    private var filters : Bundle = Bundle()
    private var nameFilter : String = ""
    private var cityFilter : String = ""
    private var priceMinFilter : Double = 0.0
    private var priceMaxFilter : Double = 100.0
    private var catITFilter : Boolean = false
    private var catBusinessFilter : Boolean = false
    private var catFinanceFilter : Boolean = false
    private var catMarketingFilter : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("new_filters") { _, bundle ->
            filters = bundle
            processFilters(bundle)
        }
    }

    private fun processFilters(bundle: Bundle) {
        cityFilter = bundle.getString("city", "")

        priceMaxFilter = bundle.getDouble("priceMax", 1000.0)
        priceMinFilter = bundle.getDouble("priceMin", 0.0)

        catITFilter = bundle.getBoolean("catIT", false)
        catBusinessFilter = bundle.getBoolean("catBusiness", false)
        catFinanceFilter = bundle.getBoolean("catFinance", false)
        catMarketingFilter = bundle.getBoolean("catMarketing", false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_search, container, false)

        view.findViewById<Button>(R.id.sortButton).setOnClickListener { showPopup(view) }
        view.findViewById<Button>(R.id.filterButton).setOnClickListener {
            setFragmentResult("old_filters", filters)
            (activity as UserMainPageActivity).setFragment(FilterFragment())
        }
        view.findViewById<ImageButton>(R.id.searchButton).setOnClickListener { onSearch(view) }

        recyclerView = view.findViewById(R.id.searchRecycler)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        adapter = SearchListAdapter(consultants, this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

        setDatabaseListener()

        return view
    }

    private fun onSearch(v : View) {
        nameFilter  = v.findViewById<EditText>(R.id.consultantNameText).text.toString()
        applyFilters()
        adapter.notifyDataSetChanged()
    }

    private fun setDatabaseListener(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                consultants.clear()
                dataSnapshot.children.mapNotNullTo(consultants) {
                    it.getValue(Consultant::class.java)
                }
                applyFilters()
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        consultantRef.addValueEventListener(postListener)
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(v.context, v.findViewById<Button>(R.id.sortButton))
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.sort_options, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.by_price_asc -> {
                    sortItems(1)
                    true
                }
                R.id.by_price_desc -> {
                    sortItems(2)
                    true
                }
                R.id.by_rating -> {
                    sortItems(3)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        popup.show()
    }

    private fun sortItems(sortOption : Int){
        when (sortOption) {
            1 -> {
                consultants.sortWith { x, y -> (getMinPrice(x) - getMinPrice(y)).toInt() }
            }
            2 -> {
                consultants.sortWith { x, y -> (getMinPrice(y) - getMinPrice(x)).toInt() }
            }
            3 -> {
                consultants.sortByDescending { it.averageRating }
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun applyFilters(){

        if (nameFilter != ""){
            consultants = consultants.filter { (it.name + " " + it.surname) == nameFilter } as MutableList<Consultant>
        }

        if (cityFilter != ""){
            consultants = consultants.filter { it.city == cityFilter } as MutableList<Consultant>
        }

        if (catITFilter || catMarketingFilter || catFinanceFilter || catBusinessFilter){
            consultants = consultants.filter { categoryFilter(it)} as MutableList<Consultant>
        }

        consultants = consultants.filter { x -> getMinPrice(x) >= priceMinFilter } as MutableList<Consultant>
        consultants = consultants.filter { x -> getMaxPrice(x) <= priceMaxFilter } as MutableList<Consultant>

        adapter.notifyDataSetChanged()
    }

    private fun getMinPrice(c : Consultant) : Double {
        var curMin = Double.MAX_VALUE
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


    private fun categoryFilter(c : Consultant) : Boolean {
        if (catITFilter && c.category.containsKey("IT")) return true
        if (catFinanceFilter && c.category.containsKey("finanse i rachunkowość")) return true
        if (catBusinessFilter && c.category.containsKey("biznes")) return true
        if (catMarketingFilter && c.category.containsKey("marketing")) return true
        return false
    }

    override fun onSearchResultClick(position: Int) {
        val myIntent = Intent(activity, UserConsultantProfileActivity::class.java)
        myIntent.putExtra("consultant_uid", consultants[position].uid)
        myIntent.putExtra("position", position.toString())
        startActivity(myIntent)
    }

}