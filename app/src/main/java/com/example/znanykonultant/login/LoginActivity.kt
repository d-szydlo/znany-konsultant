package com.example.znanykonultant.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import com.example.znanykonultant.databinding.ActivityLoginBinding
import com.example.znanykonultant.registration.RegistrationActivity
import com.example.znanykonultant.user.UserMainPageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(){
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun onLoginClick(v : View){

        val mAuth = FirebaseAuth.getInstance()
        val email = binding.loginField.text.toString()
        val password = binding.passwordField.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val userId = FirebaseAuth.getInstance().uid.toString()
                    //every user must have an email
                    Firebase.database.getReference("consultants").child(userId).get().addOnSuccessListener{
                        if(it.value != null){
                            val myintent = Intent(this, ConsultantMainPageActivity::class.java)
                            startActivity(myintent)
                            finish()
                        }
                    }

                    Firebase.database.getReference("people").child(userId).get().addOnSuccessListener{
                        if(it.value != null){
                            val myintent = Intent(this, UserMainPageActivity::class.java)
                            startActivity(myintent)
                            finish()
                        }
                    }
                }.addOnFailureListener {  e ->
                    Toast.makeText(
                        this, e.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(this, "Podaj email i hasło!", Toast.LENGTH_SHORT).show()
        }
    }

    fun onRegisterClick(v : View){
        val myintent = Intent(this, RegistrationActivity::class.java)
        startActivityForResult(myintent, 1)
    }

    fun onForgotClick(view: View) {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }


}