package com.example.znanykonultant.entity

import com.google.firebase.database.Exclude
import java.sql.Timestamp

class Messages(var message : String, var senderLogin : String, var timestamp: Timestamp) {

    @Exclude
    fun toMap() : Map<String, Any?>{
        return mapOf(
            "message" to message,
            "sender" to senderLogin,
            "timestamp" to timestamp
        )
    }
}