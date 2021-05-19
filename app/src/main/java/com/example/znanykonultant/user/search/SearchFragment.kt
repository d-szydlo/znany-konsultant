package com.example.znanykonultant.user.search

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.dao.ConsultantDAO
import com.example.znanykonultant.user.UserMainPageActivity

class SearchFragment : Fragment() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter: SearchListAdapter
    private lateinit var consultantDAO: ConsultantDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_search, container, false)
        view.findViewById<Button>(R.id.sortButton).setOnClickListener { showPopup(view) }
        view.findViewById<Button>(R.id.filterButton).setOnClickListener { (activity as UserMainPageActivity).setFragment(FilterFragment()) }

        consultantDAO = ConsultantDAO()

        recyclerView = view.findViewById(R.id.searchRecycler)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        adapter = SearchListAdapter(consultantDAO.getConsultantsList())
        //adapter.listener = this
        recyclerView.adapter = adapter
        
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

        return view
    }

    fun showPopup(v: View) {
        val popup = PopupMenu(this.context, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.sort_options, popup.menu)
        popup.show()
    }

}