package com.example.znanykonultant.db

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp
import java.util.*
@IgnoreExtraProperties
class Appointments(var personLogin : String,
                   var consultantLogin : String,
                   var timestamp: Timestamp,
                   var place : String,
                   var rate : Int = -1
) {

    @Exclude
    fun toMap() : Map<String, Any?>{
        return mapOf(
            personLogin to true,
            consultantLogin to true,
            "rate" to rate,
            "timestamp" to timestamp,
            "place" to place,
            "rate" to rate
        )
    }
}