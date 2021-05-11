package com.example.znanykonultant.dao

import com.example.znanykonultant.entity.Consultant
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ConsultantDAO {

    fun addConsultant(login : String,
                      phone : String,
                      city : String,
                      street : String = "",
                      houseNumber : String = "",
                      description : String = "",
                      page : String = ""

    ){
        val database = Firebase.database
        val myRef = database.getReference("consultant")
        myRef.child(login).setValue(
            Consultant(
                phone,
                city,
                street,
                houseNumber,
                description,
                page
            )
        )

    }

}