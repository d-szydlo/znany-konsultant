package com.example.znanykonultant.dao

import android.util.Log
import com.example.znanykonultant.entity.Category
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.WorkDays
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class ConsultantDAO {

    val database = Firebase.database
    val consultantRef = database.getReference("consultants")

    val consultantUid = FirebaseAuth.getInstance().uid     // after auth change this to curr user

    var data : Consultant? = null
    var consultants : MutableList<Consultant> = mutableListOf()


    init {
        /**
         *  THIS IS JUST A SAMPLE!
         * */
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                data = dataSnapshot.child(consultantUid!!).getValue(Consultant::class.java)

                // consultants data may be public
                dataSnapshot.children.mapNotNullTo(consultants) {
                    it.getValue(Consultant::class.java)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }

        consultantRef.addValueEventListener(postListener)
    }

    fun addConsultant(uid : String,
                      name : String,
                      surname : String,
                      email : String,
                      picture : String = "",
                      phone : String,
                      city : String,
                      street : String = "",
                      houseNumber : String = "",
                      description : String = "",
                      page : String = "",
                      averageRating : Float = 0f,
                      workTime : Map<String, WorkDays> = mapOf(),
                      categories : MutableList<String> = mutableListOf()

    ){

        consultantRef.child(uid).setValue(
            Consultant(
                uid,
                name,
                surname,
                email,
                picture,
                phone,
                city,
                street,
                houseNumber,
                description,
                page,
                averageRating,
                workTime,
                Category(categories).toMap()
            )
        )

    }

    fun modifyPersonalData(consultantUpdate : MutableMap<String, Any>) {
        if (consultantUid != null) {
            consultantRef.child(consultantUid).updateChildren(consultantUpdate)
        }
        //var consultantUpdate = mutableMapOf<String, Any>()
        //consultantUpdate["averageRating"] = newAvg
        //consultantRef.child(consultantUid).updateChildren(consultantUpdate)
    }

    fun getPersonData(): Consultant?{
        return data
    }

    fun deletePerson() {
        // should work
        if (consultantUid != null) {
            consultantRef.child(consultantUid).removeValue()
        }
    }

    fun getConsultantsList(): MutableList<Consultant> {
        return consultants
    }

}