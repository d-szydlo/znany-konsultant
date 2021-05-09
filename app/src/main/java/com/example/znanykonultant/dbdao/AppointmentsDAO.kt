package com.example.znanykonultant.dbdao

import android.content.ContentValues.TAG
import android.util.Log
import com.example.znanykonultant.db.Appointments
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.Console
import java.sql.Timestamp

class AppointmentsDAO {
    val database = Firebase.database

    fun addAppointment(user : String,
                       consultant : String,
                       timestamp: Timestamp,
                       place : String


    ) {
        val myRef = database.getReference("person")
        val appointRef = database.getReference("appointments")

        myRef.child(user).child("appointments").setValue({appointRef.key to true})
        myRef.child(consultant).child("appointments").setValue({appointRef.key to true})

    }
}