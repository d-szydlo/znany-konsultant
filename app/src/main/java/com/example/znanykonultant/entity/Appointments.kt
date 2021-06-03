package com.example.znanykonultant.entity

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp

@IgnoreExtraProperties
class Appointments(var person : String = "",
                   var consultant : String = "",
                   var personID : String =  "",
                   var consultantID : String =  "",
                   var timestampStart: Long = System.nanoTime(),
                   var timestampStop: Long = System.nanoTime(),
                   var place : String = "",
                   var confirmed : Boolean = false,
                   var rate : Int = -1,

) {

}