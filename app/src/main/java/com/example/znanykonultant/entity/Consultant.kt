package com.example.znanykonultant.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Consultant(
                var uid: String = "",
                var name: String = "",
                var surname: String = "",
                var email: String = "",
                var picture: String = "",
                var phone : String = "",
                var city : String = "",
                var street : String = "",
                var houseNumber : String = "",
                var description : String = "",
                var page : String = "",
                var averageRating : Float = 0f,
                var category: Map<String, Boolean> = mapOf(),
                var rating : Map<Int, Boolean> = mapOf(),
                var consultantService : Map<String, ConsultantService> = mapOf(),
                var appointments: Appointments? = null,
                var favourites:  Map<String, Favourite> = mapOf(),
                 ) {}