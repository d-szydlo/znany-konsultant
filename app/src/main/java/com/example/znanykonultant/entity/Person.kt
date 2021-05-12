package com.example.znanykonultant.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Person(
    var name: String = "",
    var surname: String = "",
    var picture: String = "",
    var appointments: Appointments? = null,
    var favourites:  Map<Int, Favourite> = mapOf()
) {



}

