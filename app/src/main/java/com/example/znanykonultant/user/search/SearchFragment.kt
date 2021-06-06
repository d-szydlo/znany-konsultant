package com.example.znanykonultant.user.search

import android.content.Intent
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
    private var filterConsultants : MutableList<Consultant> = mutableListOf()
    var filters : Bundle = Bundle()

    private fun processFilters(bundle: Bundle) {
        adapter.cityFilter = bundle.getString("city", "")

        adapter.priceMaxFilter = bundle.getDouble("priceMax", 1000.0)
        adapter.priceMinFilter = bundle.getDouble("priceMin", 0.0)

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

        setButtonsOnClick(view)
        prepareRecycler(view)
        setDatabaseListener()

        if (activity?.intent != null){
            val bundle = activity?.intent!!.getBundleExtra("filters")
            if (bundle != null){
                filters = bundle
                processFilters(bundle)
            }
        }

        return view
    }

    private fun setButtonsOnClick(view: View) {
        view.findViewById<Button>(R.id.sortButton).setOnClickListener { showPopup(view) }
        view.findViewById<ImageButton>(R.id.searchButton).setOnClickListener { onSearch(view) }
        view.findViewById<Button>(R.id.filterButton).setOnClickListener { onFilter() }
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(v.context, v.findViewById<Button>(R.id.sortButton))
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.sort_options, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.by_price_asc -> {
                    adapter.sortItems(1)
                    true
                }
                R.id.by_price_desc -> {
                    adapter.sortItems(2)
                    true
                }
                R.id.by_rating -> {
                    adapter.sortItems(3)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        popup.show()
    }

    private fun onFilter() {
        val intent = Intent(activity, UserFilterActivity::class.java)
        intent.putExtra("filters", filters)
        startActivity(intent)
    }

    private fun onSearch(v : View) {
        adapter.nameFilter  = v.findViewById<EditText>(R.id.consultantNameText).text.toString()
        adapter.applyFilters()
    }

    private fun prepareRecycler(view: View) {
        recyclerView = view.findViewById(R.id.searchRecycler)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        adapter = SearchListAdapter(filterConsultants, consultants, this)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                view.context,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.applyFilters()
    }

    private fun setDatabaseListener(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                consultants.clear()
                dataSnapshot.children.mapNotNullTo(consultants) {
                    it.getValue(Consultant::class.java)
                }
                adapter.applyFilters()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        consultantRef.addValueEventListener(postListener)
    }

    override fun onSearchResultClick(position: Int) {
        val myIntent = Intent(activity, UserConsultantProfileActivity::class.java)
        myIntent.putExtra("consultant_uid", filterConsultants[position].uid)
        startActivity(myIntent)
    }

    companion object {
        const val PRICE_MAX_DEFAULT = 1000.0
        const val PRICE_MIN_DEFAULT = 0.0
    }
}