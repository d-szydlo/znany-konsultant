package com.example.znanykonultant.db

import com.google.firebase.database.Exclude

class Favourite(var userLogin : String) {

    @Exclude
    fun toMap() : Map<String, Any?>{
        return mapOf(
            userLogin to true
        )
    }

}