package com.example.znanykonultant.dao

import android.util.Log
import com.example.znanykonultant.entity.Category
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.Person
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class ConsultantDAO {

    val database = Firebase.database
    private val consultantRef = database.getReference("consultant")

    private val user = "ziom123"     // after auth change this to curr user

    var data : Consultant? = null
    var consultants : MutableList<Consultant> = mutableListOf()


    init {
        /**
         *  THIS IS JUST A SAMPLE!
         * */
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                data = dataSnapshot.child(user).getValue(Consultant::class.java)

                // consultants data may be public
                dataSnapshot.children.mapNotNullTo(consultants) {
                    it.getValue(Consultant::class.java)
                }
                Log.i("firebase", data.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }

        consultantRef.addValueEventListener(postListener)
    }

    fun addConsultant(login : String,
                      name : String,
                      surname : String,
                      picture : String = "",
                      phone : String,
                      city : String,
                      street : String = "",
                      houseNumber : String = "",
                      description : String = "",
                      page : String = "",
                      categories : MutableList<String> = mutableListOf()

    ){

        consultantRef.child(login).setValue(
            Consultant(
                name,
                surname,
                picture,
                phone,
                city,
                street,
                houseNumber,
                description,
                page,
                Category(categories).toMap()
            )
        )

    }

    fun modifyPersonalData(consultantUpdate : MutableMap<String, Any>) {
        consultantRef.child(user).updateChildren(consultantUpdate)
    }

    fun getPersonData(): Consultant?{
        return data
    }

    fun deletePerson() {
        // should work
        consultantRef.child(user).removeValue()
    }

    fun getConsultantsList(): MutableList<Consultant> {
        return consultants
    }

}