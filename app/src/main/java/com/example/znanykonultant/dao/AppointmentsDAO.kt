package com.example.znanykonultant.dao

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
        val myRef = database.getReference("person")
        val appointRef = database.getReference("appointments")

        myRef.child(user).child("appointments").setValue({appointRef.key to true})
        myRef.child(consultant).child("appointments").setValue({appointRef.key to true})

    }
}