package com.example.znanykonultant.entity

import com.google.firebase.database.Exclude

class Category(private val categories : MutableList<String>) {

        @Exclude
        fun toMap() : Map<String, Boolean>{
            val map : MutableMap<String, Boolean> = mutableMapOf()
            for (category in categories){
                map[category] = true
            }
            return map
        }
}