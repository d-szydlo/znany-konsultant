package com.example.znanykonultant.entity

import com.google.firebase.database.Exclude

class Favourite(var userLogin : String) {

    @Exclude
    fun toMap() : Map<String, Any?>{
        return mapOf(
            userLogin to true
        )
    }

}