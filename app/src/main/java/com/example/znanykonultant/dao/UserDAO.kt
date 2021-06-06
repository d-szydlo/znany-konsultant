package com.example.znanykonultant.dao
import android.util.Log
import com.example.znanykonultant.entity.Favourite
import com.example.znanykonultant.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class UserDAO {

    val database = Firebase.database
    val personRef = database.getReference("users")

    val personUid = FirebaseAuth.getInstance().uid  // after auth change this to curr user,

    var data : User? = null

    init {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                data = dataSnapshot.child(personUid!!).getValue(User::class.java)
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
        uid : String,
        name : String,
        surname : String,
        email : String,
        phone : String,
        picture: String = "",
    ){
        personRef.child(uid).setValue(
            User(
                uid,
                name,
                surname,
                email,
                phone,
                picture
            )
        )
    }


    fun modifyPersonalData(personUpdate : MutableMap<String, Any>) {
//        val personUpdate: MutableMap<String, Any> = HashMap()
//        personUpdate["name"] = "Oli"
        if (personUid != null) {
            personRef.child(personUid).updateChildren(personUpdate)
        }
    }

    fun getPersonData(): User?{
        return data
    }

    fun deletePerson() {
        // should work
        if (personUid != null) {
            personRef.child(personUid).removeValue()
        }
    }

    fun addFavourite() {
        val favourite = Favourite("olooli123").toMap()
        if (personUid != null) {
            personRef.child(personUid).child("favourites").updateChildren(favourite)
        }

    }

}