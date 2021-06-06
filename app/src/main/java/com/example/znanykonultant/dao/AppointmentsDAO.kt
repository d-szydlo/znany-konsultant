package com.example.znanykonultant.dao

import com.example.znanykonultant.entity.Appointments
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AppointmentsDAO {
    val database = Firebase.database
    private val appointRef = database.getReference("appointments")
    private val userRef = database.getReference("users")
    private val consultantRef = database.getReference("consultants")
    fun addAppointment(user : String,
                       consultant : String,
                       timestampStart: Long,
                       timestampStop: Long,
                       place : String,
                       username : String,
                       consultantname : String,
                       type : String

    ) {
        val pushedRef = appointRef.push()

        // generated key
        val appointID = pushedRef.key.toString()


        // add ref to user
        userRef.child(user).child("appointments").child(appointID).setValue(true)
        consultantRef.child(consultant).child("appointments").child(appointID).setValue(true)

        // push() generates auto id
        pushedRef.setValue(
            Appointments(
                appointID,
                username,
                consultantname,
                user,
                consultant,
                timestampStart,
                timestampStop,
                place,
                type
            )
        )



    }

    fun modifyAppointment(update : MutableMap<String, Any>, appointmentId: String) {
        appointRef.child(appointmentId).updateChildren(update)
    }

    fun deleteAppointment(appointmentId : String, personID : String, consultantID: String) {
        appointRef.child(appointmentId).removeValue()
        userRef.child(personID).child("appointments").child(appointmentId).removeValue()
        consultantRef.child(consultantID).child("appointments").child(appointmentId).removeValue()
    }

}