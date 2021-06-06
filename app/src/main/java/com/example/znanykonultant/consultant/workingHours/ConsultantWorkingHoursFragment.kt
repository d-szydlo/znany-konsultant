package com.example.znanykonultant.consultant.workingHours

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.WorkDays
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConsultantWorkingHoursFragment : Fragment() {
    private val consultantUid = FirebaseAuth.getInstance().uid
    private val workDaysRef = FirebaseDatabase.getInstance().getReference("consultants/$consultantUid/worktime")
    private val workDaysList : MutableList<WorkDays> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_consultant_working_hours, container, false)
        view.findViewById<Button>(R.id.add_working_hours_button).setOnClickListener { addNewHours() }
        listenForNewWorkDays(view)
        return view
    }

    private fun listenForNewWorkDays(view: View) {
        workDaysRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                workDaysList.clear()
                snapshot.children.mapNotNullTo(workDaysList) { it.getValue(WorkDays::class.java) }

                val servicesRecycler = view.findViewById<RecyclerView>(R.id.consultant_working_hours_recycler)
                servicesRecycler.layoutManager = LinearLayoutManager(view.context)
                servicesRecycler.adapter = ConsultantWorkingHoursAdapter(view.context, workDaysList)
                servicesRecycler.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun addNewHours() {
        val intent = Intent(activity, ConsultantEditWorkingHoursActivity::class.java)
        startActivity(intent)
    }
}
