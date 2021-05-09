package com.example.znanykonultant.dbdao

import com.example.znanykonultant.db.Consultant
import com.example.znanykonultant.db.ConsultantService
import com.example.znanykonultant.db.Person
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