package com.example.znanykonultant.consultant.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.consultant.profile.ConsultantServicesAdapter
import com.example.znanykonultant.entity.ConsultantService

class ConsultantServicesFragment : Fragment() {

    lateinit var servicesRecycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_consultant_services, container, false)

        var consultant: ConsultantService = ConsultantService(120.0, "bardzo dlugie konsultacje", "dlugie")
        var consultant2: ConsultantService = ConsultantService(150.0, "kons", "rozmowa")
        var servicesList = arrayListOf(consultant)
        servicesList.add(consultant2)

        servicesRecycler = view.findViewById<RecyclerView>(R.id.consultant_services)
        servicesRecycler.layoutManager = LinearLayoutManager(view.context)
        servicesRecycler.adapter = ConsultantServicesAdapter(view.context, servicesList)
        servicesRecycler.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

        servicesRecycler.addOnLayoutChangeListener(View.OnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                servicesRecycler.postDelayed(Runnable {
                    servicesRecycler.smoothScrollToPosition(
                            (servicesRecycler.adapter as ConsultantServicesAdapter).itemCount - 1
                    )
                }, 100)
            }
        })

        return view
    }

    private fun addService(view: View){

    }
}