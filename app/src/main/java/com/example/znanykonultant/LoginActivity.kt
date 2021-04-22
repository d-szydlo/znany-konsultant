package com.example.znanykonultant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

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
    }

    fun onLoginClick(v : View){
        var login = loginField.text
        var password = passwordField.text
        //var intent = Intent()
    }
}