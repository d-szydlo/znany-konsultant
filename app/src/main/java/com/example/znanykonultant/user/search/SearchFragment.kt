package com.example.znanykonultant.user.search

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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter: SearchListAdapter
    private val database = Firebase.database
    private val consultantRef = database.getReference("consultants")
    private var consultants : MutableList<Consultant> = mutableListOf()
    private var filters : Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("new_filters") { _, bundle ->
            filters = bundle
            processFilters(bundle)
        }
    }

    private fun processFilters(bundle: Bundle) {
        adapter.cityFilter = bundle.getString("city", "")

        adapter.priceMaxFilter = bundle.getDouble("priceMax", 1000.0)
        adapter.priceMinFilter = bundle.getDouble("priceMin", 0.0)

        adapter.morningFilter = bundle.getBoolean("hoursMorning", false)
        adapter.afternoonFilter = bundle.getBoolean("hoursAfternoon", false)
        adapter.eveningFilter = bundle.getBoolean("hoursEvening", false)

        adapter.catITFilter = bundle.getBoolean("catIT", false)
        adapter.catBusinessFilter = bundle.getBoolean("catBusiness", false)
        adapter.catFinanceFilter = bundle.getBoolean("catFinance", false)
        adapter.catMarketingFilter = bundle.getBoolean("catMarketing", false)
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
        adapter = SearchListAdapter(consultants)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

        setDatabaseListener()

        return view
    }

    private fun onSearch(v : View) {
        adapter.nameFilter  = v.findViewById<EditText>(R.id.consultantNameText).text.toString()
        adapter.setData(consultants)
    }

    private fun setDatabaseListener(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(consultants) {
                    it.getValue(Consultant::class.java)
                }
                adapter.setData(consultants)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        consultantRef.addValueEventListener(postListener)
    }

    fun showPopup(v: View) {
        val popup = PopupMenu(v.context, v.findViewById<Button>(R.id.sortButton))
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.sort_options, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.by_availability -> {
                    adapter.sortItems(1)
                    true
                }
                R.id.by_price_asc -> {
                    adapter.sortItems(2)
                    true
                }
                R.id.by_price_desc -> {
                    adapter.sortItems(3)
                    true
                }
                R.id.by_rating -> {
                    adapter.sortItems(4)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        popup.show()
    }

}