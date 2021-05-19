package com.example.znanykonultant.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.znanykonultant.R
import com.example.znanykonultant.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
    }

    fun showUserFragment(view: View) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.registrationFragment, UserRegisterFragment())
        transaction.commit()
    }

    fun showConsultantFragment(view: View) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.registrationFragment, ConsultantRegisterFragment())
        transaction.commit()
    }

    fun goBack(view: View){
        finish()
    }
}