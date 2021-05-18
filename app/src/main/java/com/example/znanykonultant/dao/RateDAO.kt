package com.example.znanykonultant.dao

import com.example.znanykonultant.entity.Rate
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class RateDAO {
    val database = Firebase.database
    private val rateRef = database.getReference("rating")

    fun addRating(
        consultantLogin : String,
        userLogin : String,
        value : Int,
        description : String,
        date : Date
    ) {
        val pushedRef = rateRef.push()

        // generated key
        val rateID = pushedRef.key.toString()

        val userRef = database.getReference("person")
        val consultantRef = database.getReference("consultant")

        pushedRef.setValue(
            Rate(
                consultantLogin,
                userLogin,
                value,
                description,
                date
            ).toMap()
        )

        userRef.child(userLogin).child("ratings").child(rateID).setValue(true)
        consultantRef.child(consultantLogin).child("ratings").child(rateID).setValue(true)
    }

}