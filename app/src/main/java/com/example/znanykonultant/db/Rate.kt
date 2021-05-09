package com.example.znanykonultant.db

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*


@IgnoreExtraProperties
class Rate(var consultantLogin : String,
           var userLogin : String,
           var value : Int,
           var description : String,
           var date : Date
) {

    @Exclude
    fun toMap() : Map<String, Any?>{
        return mapOf(
            consultantLogin to true,
            userLogin to true,
            "value" to value,
            "description" to description,
            "date" to date
        )
    }
}