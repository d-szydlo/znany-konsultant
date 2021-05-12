package com.example.znanykonultant.dao

import android.util.Log
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.Person
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp

class AppointmentsDAO {
    val database = Firebase.database
    fun addAppointment(user : String,
                       consultant : String,
                       timestamp: Timestamp,
                       place : String


    ) {
        val userRef = database.getReference("person")
        val consultantRef = database.getReference("consultant")
        val appointRef = database.getReference("appointments")
        val pushedRef = appointRef.push()

        // generated key
        val appointID = pushedRef.key.toString()

        // push() generates auto id
        pushedRef.setValue(
            Appointments(
                user,
                consultant,
                timestamp,
                place
            )
        )

        // add ref to user
        userRef.child(user).child("appointments").child(appointID).setValue(true)
        consultantRef.child(consultant).child("appointments").child(appointID).setValue(true)

    }

    // single get option
    fun getAppointments(user: String) {
        val userRef = database.getReference("person")
        var data : Any? = "hejcia"

        /**
         *  THIS IS JUST A SAMPLE!
         * */
        val appointments = userRef.child(user).child("appointments").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.key}")
            data = it.value
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }


        Log.i("firebase", data as String)


    }

}