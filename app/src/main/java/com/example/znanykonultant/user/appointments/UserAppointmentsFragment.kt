package com.example.znanykonultant.user.appointments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.entity.WorkDays
import com.example.znanykonultant.tools.DateTimeConverter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserAppointmentsFragment : Fragment(), AppointmentsAdapter.OnItemClickListener {

    private lateinit var listAdapter: AppointmentsAdapter
    private var data : MutableList<Appointments> = mutableListOf()
    private var appointmentIds : MutableList<String> = mutableListOf()
    private val database = Firebase.database
    private val userRef = database.getReference("users")
    private val appointmentsRef = database.getReference("appointments")


    private val userUID = FirebaseAuth.getInstance().uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_appointments, container, false)
        listAdapter = AppointmentsAdapter(data, this)

        setDatabaseListener()
        initRecycler(view)



        return view
    }

    private fun initRecycler(view: View){
        val recyclerView = view.findViewById<RecyclerView>(R.id.appointmentsRecycler)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = listAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
    }

    private fun setDatabaseListener(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                appointmentIds = mutableListOf()

                dataSnapshot.child(userUID!!).child("appointments").children.mapNotNullTo(appointmentIds)
                {
                   it.key
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }



        val appointmentListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                data = mutableListOf()
                for (key in appointmentIds) {
                    val appointment = dataSnapshot.child(key).getValue(Appointments::class.java)

                    if (appointment != null) {
                        data.add(appointment)
                    }
                }

                listAdapter.updateData(data)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }

        userRef.addValueEventListener(postListener)
        appointmentsRef.addValueEventListener(appointmentListener)

    }

    override fun onItemClick(appointment: Appointments) {

        val sendData = Bundle()

        sendData.putString("id", appointment.id)
        sendData.putString("consultant", appointment.consultant)
        sendData.putString("clientID", appointment.personID)
        sendData.putString("consultantID", appointment.consultantID)
        sendData.putLong("dateStart", appointment.timestampStart)
        sendData.putLong("dateStop", appointment.timestampStop)
        sendData.putString("place", appointment.place)
        sendData.putString("type", appointment.type)
        sendData.putBoolean("confirmed", appointment.confirmed)
        sendData.putSerializable("terms", calculateTerms(appointment))


        val myIntent = Intent(activity, UserAppointmentsVisitsActivity::class.java)
        myIntent.putExtra("data", sendData)
        startActivityForResult(myIntent, 1)

    }

    private fun calculateTerms(appointment: Appointments) : HashMap<String, MutableList<WorkDays>> {
        val output : HashMap<String, MutableList<WorkDays>> = hashMapOf()
        val newData = data.toMutableList()
        newData.remove(appointment)
        for (app in newData) {
            val dateStart = DateTimeConverter(app.timestampStart).splitConverted()
            val dateStop = DateTimeConverter(app.timestampStop).splitConverted()

            if(output.containsKey(dateStart[0]))
                output[dateStart[0]]?.add(WorkDays("", "",dateStart[1], dateStop[1]))
            else
                output[dateStart[0]] = mutableListOf(WorkDays("", "", dateStart[1], dateStop[1]))
        }

        return output

    }
}
