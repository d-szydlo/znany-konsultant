package com.example.znanykonultant.entity

import com.google.firebase.database.Exclude
import java.sql.Timestamp

class PageVisit(var visitor : String = "",
                var consultant: String = "",
                var timestamp: Long = 0) {

    @Exclude
    fun toMap() : Map<String, Any?>{
        return mapOf(
            visitor to true,
            consultant to true,
            "timestamp" to timestamp,
        )
    }
}