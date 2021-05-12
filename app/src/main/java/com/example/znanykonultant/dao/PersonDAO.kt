package com.example.znanykonultant.dao
import android.util.Log
import com.example.znanykonultant.entity.Favourite
import com.example.znanykonultant.entity.Person
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class PersonDAO {

    val database = Firebase.database
    private val personRef = database.getReference("person")

    private val user = "hihi"     // after auth change this to curr user

    var data : Person? = null

    init {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                data = dataSnapshot.child(user).getValue(Person::class.java)
                Log.i("firebase", data.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }

        personRef.addValueEventListener(postListener)
    }

    // TODO delete login if curr user will be available
    fun addPerson(
        login: String,
        name: String,
        surname: String,
        picture: String = "",
    ){
        personRef.child(login).setValue(
            Person(
                name,
                surname,
                picture
            )
        )
    }


    fun modifyPersonalData(personUpdate : MutableMap<String, Any>) {
//        val personUpdate: MutableMap<String, Any> = HashMap()
//        personUpdate["name"] = "Oli"
        personRef.child(user).updateChildren(personUpdate)
    }

    fun getPersonData(): Person?{
        return data
    }

    fun deletePerson() {
        // should work
        personRef.child(user).removeValue()
    }

    fun addFavourite() {
        val favourite = Favourite("olooli123").toMap()
        personRef.child(user).child("favourites").updateChildren(favourite)

    }

}