package com.example.znanykonultant.dbdao

import com.example.znanykonultant.db.Consultant
import com.example.znanykonultant.db.ConsultantService
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ConsultantDAO {

    fun addConsultant(){
        val database = Firebase.database
        val myRef = database.getReference("consultant")

        var users =  HashMap<String, Consultant>()
        val cs = HashMap<Int, ConsultantService>()
        cs.put(1, ConsultantService(1,"Hejcia", "Typ"))
        users.put("ziom123", Consultant("123", "Wro", "12",
            "12", "HI", "Person", cs)
        )
        myRef.setValue(users);

    }
}