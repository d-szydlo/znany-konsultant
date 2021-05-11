package com.example.znanykonultant.entity

import com.google.firebase.database.Exclude

class Members (var users : List<String>) {

    @Exclude
    fun toMap() : Map<String, Any?>{
        var map : MutableMap<String, Any?> = mutableMapOf()
        for (user in users){
            map[user] = true
        }
        return map
    }
}