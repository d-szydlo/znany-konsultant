package com.example.znanykonultant.dbdao
import com.example.znanykonultant.db.Person
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PersonDAO {

    fun addPerson(){
        val database = Firebase.database
        val myRef = database.getReference("person")

        var users =  HashMap<String, Person>()
        users.put("domkakromka", Person("Domka", "Kromka", "Hejcia123",
        "", 0, "Person")
        )
        myRef.setValue(users);

    }
}