package com.example.znanykonultant

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import com.example.znanykonultant.login.LoginActivity
import com.example.znanykonultant.user.UserMainPageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val uid = FirebaseAuth.getInstance().uid
        if (uid != null) {
            checkUserOrConsultant()
        } else {
            startLogin()
        }
    }

    fun checkUserOrConsultant(){
        val userId = FirebaseAuth.getInstance().uid.toString()
        //every user must have an email
        Firebase.database.getReference("consultants").child(userId).get().addOnSuccessListener{
            if(it.value != null){
                val myintent = Intent(this, ConsultantMainPageActivity::class.java)
                startActivity(myintent)
                finish()
            }
        }.addOnFailureListener {
            startLogin()
        }

        Firebase.database.getReference("users").child(userId).get().addOnSuccessListener{
            if(it.value != null){
                val myintent = Intent(this, UserMainPageActivity::class.java)
                startActivity(myintent)
                finish()
            }
        }.addOnFailureListener {
            startLogin()
        }
    }

    fun startLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}