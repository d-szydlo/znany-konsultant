package com.example.znanykonultant.dao
import com.example.znanykonultant.entity.Person
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PersonDAO {
    val database = Firebase.database
    fun addPerson(login : String,
                  name : String,
                  surname : String,
                  password : String,
                  picture : Int = 0,
                  email : String = "",
                  type : String = "Person"
    ){
        val myRef = database.getReference("person")
        myRef.child(login).setValue(
            Person(
                name,
                surname,
                password,
                email,
                picture,
                type
            )
        )
    }

}