package com.example.znanykonultant.consultant.appointments

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
import com.example.znanykonultant.appointments.CommonFunctions
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.user.appointments.AppointmentsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ConsultantAppointmentsFragment : Fragment(), AppointmentsAdapter.OnItemClickListener {

    private lateinit var listAdapter: AppointmentsAdapter
    private var data : MutableList<Appointments> = mutableListOf()
    private var appointmentIds : MutableList<String> = mutableListOf()

    private val userUID = FirebaseAuth.getInstance().uid

    private val database = Firebase.database
    private val consultantRef = database.getReference("consultants")
    private val appointmentsRef = database.getReference("appointments")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_appointments, container, false)
        listAdapter = AppointmentsAdapter(data,this)
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

        consultantRef.addValueEventListener(postListener)
        appointmentsRef.addValueEventListener(appointmentListener)

    }




    override fun onItemClick(appointment: Appointments) {

        val sendData = Bundle()
        val f = CommonFunctions()

        sendData.putString("id", appointment.id)
        sendData.putString("client", appointment.person)
        sendData.putString("clientID", appointment.personID)
        sendData.putString("consultantID", appointment.consultantID)
        sendData.putLong("dateStart", appointment.timestampStart)
        sendData.putLong("dateStop", appointment.timestampStop)
        sendData.putString("place", appointment.place)
        sendData.putString("type", appointment.type)
        sendData.putBoolean("confirmed", appointment.confirmed)
        sendData.putSerializable("terms", f.calculateTerms(data))

        val myIntent = Intent(activity, ConsultantAppointmentsVisitsActivity::class.java)
        myIntent.putExtra("data", sendData)
        startActivity(myIntent)

    }
}
