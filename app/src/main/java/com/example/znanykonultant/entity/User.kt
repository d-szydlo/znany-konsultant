package com.example.znanykonultant.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User (
    var uid: String = "",
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var phone: String= "",
    var picture: String = "",
    var appointments: Appointments? = null,
    var favourites:  Map<Int, Favourite> = mapOf()
) : Human() {
    override fun getFullName() = "$name $surname"
}
