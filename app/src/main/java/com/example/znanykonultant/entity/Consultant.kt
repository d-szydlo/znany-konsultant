package com.example.znanykonultant.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Consultant(
                var name: String,
                var surname: String,
                var picture: String,
                var phone : String,
                var city : String,
                var street : String,
                var houseNumber : String,
                var description : String,
                var page : String,
                var category: Map<String, Boolean> = mapOf(),
                var rating : Map<Int, Boolean> = mapOf(),
                var consultantService : Map<Int, ConsultantService> = mapOf(),
                var appointments: Appointments? = null,
                var favourites:  Map<Int, Favourite> = mapOf(),
                 ) {}