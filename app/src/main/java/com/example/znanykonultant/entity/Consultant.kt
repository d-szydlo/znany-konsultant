package com.example.znanykonultant.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Consultant(val phone : String,
                 val city : String,
                 val street : String,
                 val houseNumber : String,
                 val description : String,
                 val page : String,
                 val rating : Map<Int, Boolean> = mapOf(),
                 val consultantService : Map<Int, ConsultantService> = mapOf()
                 ) {}