package com.example.znanykonultant.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.znanykonultant.databinding.ActivityLoginBinding
import com.example.znanykonultant.registration.RegistrationActivity
import com.example.znanykonultant.user.UserMainPageActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(){
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun onLoginClick(v : View){
        var mAuth = FirebaseAuth.getInstance()
        var email = binding.loginField.text.toString()
        var password = binding.passwordField.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val myintent = Intent(this, UserMainPageActivity::class.java)
                    startActivity(myintent)
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


}