package com.example.znanykonultant.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Person(
    var name: String,
    var surname: String,
    var password: String,
    var email: String = "",
    var picture: Int = 0,
    var type: String,
    var appointments: Appointments? = null,
    var favourites:  Map<Int, Favourite> = mapOf()
) {



}

