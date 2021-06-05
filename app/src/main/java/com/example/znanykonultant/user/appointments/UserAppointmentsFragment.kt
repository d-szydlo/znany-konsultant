package com.example.znanykonultant.user.appointments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.User
import com.example.znanykonultant.entity.WorkDays
import com.example.znanykonultant.tools.DateTimeConverter
import com.example.znanykonultant.user.UserMainPageActivity
import com.example.znanykonultant.user.search.SearchFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.Serializable

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
        listAdapter = AppointmentsAdapter(data, appointmentIds, this)

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
                data = mutableListOf()

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
                    Log.e("firebase", key)
                    val appointment = dataSnapshot.child(key).getValue(Appointments::class.java)

                    if (appointment != null) {
                        data.add(appointment)
                    }
                    Log.e("firebase", appointment.toString())
                }

                listAdapter.updateData(data, appointmentIds)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }

        userRef.addValueEventListener(postListener)
        appointmentsRef.addValueEventListener(appointmentListener)

    }



    override fun onItemClick(appointment: Appointments, id : String) {

        val sendData = Bundle()

        sendData.putString("id", id)
        sendData.putString("consultant", appointment.consultant)
        sendData.putString("clientID", appointment.personID)
        sendData.putString("consultantID", appointment.consultantID)
        sendData.putLong("dateStart", appointment.timestampStart)
        sendData.putLong("dateStop", appointment.timestampStop)
        sendData.putString("place", appointment.place)
        sendData.putBoolean("confirmed", appointment.confirmed)
        sendData.putSerializable("terms", calculateTerms())

        setFragmentResult("data", sendData)
        (activity as UserMainPageActivity).setFragment(UserAppointmentsSignInFragment())
    }

    private fun calculateTerms() : HashMap<String, String> {
        val output :  HashMap<String, String> = hashMapOf()
        for (app in data) {
            val dateStart = DateTimeConverter(app.timestampStart).splitConverted()
            val dateStop = DateTimeConverter(app.timestampStop).splitConverted()
            if(output.containsKey(dateStart[0]))
                output[dateStart[0]] += "\n${dateStart[1]} ${dateStop[1]}"
            else
                output[dateStart[0]] = "${dateStart[1]} ${dateStop[1]}"
        }

        return output

    }
}
