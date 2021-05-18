package com.example.znanykonultant.consultant.appointments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R

class ConsultantAppointmentsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_consultant_appointments, container, false)
        loadRecycler(view)
        return view
    }

    private fun loadRecycler(view: View) {
        val appointmentsRecycler = view.findViewById<RecyclerView>(R.id.appointments_recycler)
        appointmentsRecycler.layoutManager = LinearLayoutManager(activity)
        appointmentsRecycler.adapter = ConsultantAppointmentsListAdapter(genTempData())
    }

    private fun genTempData(): List<String> {
        return listOf("abc", "def", "xyz")
    }
}
