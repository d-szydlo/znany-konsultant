package com.example.znanykonultant.dao

import com.example.znanykonultant.entity.PageVisit
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.util.*

class PageVisitDAO {

    val database = Firebase.database
    private val pageRef = database.getReference("pagevisited")

    fun addPageVisited(
        consultantLogin : String,
        userLogin : String,
        timestamp: Timestamp,
    ) {
        val pushedRef = pageRef.push()

        // generated key
        val visitID = pushedRef.key.toString()

        val userRef = database.getReference("person")
        val consultantRef = database.getReference("consultant")

        pushedRef.setValue(
            PageVisit(
                consultantLogin,
                userLogin,
                timestamp
            ).toMap()
        )

        userRef.child(userLogin).child("pagevisited").child(visitID).setValue(true)
        consultantRef.child(consultantLogin).child("pagevisited").child(visitID).setValue(true)
    }
}