package com.example.znanykonultant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.znanykonultant.dbdao.ConsultantDAO
import com.example.znanykonultant.dbdao.PersonDAO
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(){

    private lateinit var loginField : EditText
    private lateinit var passwordField : EditText
    private lateinit var loginButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginField = findViewById(R.id.loginField)
        passwordField = findViewById(R.id.passwordField)
        loginButton = findViewById(R.id.loginButton)

//
//        val database = Firebase.database
//        val myRef = database.getReference("consultant")
//        myRef.setValue("Hihi")
//
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val value = snapshot.value
//                loginField.setText("Value is $value")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })




    }

    fun onLoginClick(v : View){
        var login = loginField.text
        var password = passwordField.text

//        val personDao = PersonDAO()
//        val consultantDAO = ConsultantDAO()
//        personDao.addPerson()
//        consultantDAO.addConsultant()

        val myintent = Intent(this, MainPageActivity::class.java)
        startActivityForResult(myintent, 1)

    }

    fun onRegisterClick(v : View){
        val myintent = Intent(this, RegistrationActivity::class.java)
        startActivityForResult(myintent, 1)
    }


}