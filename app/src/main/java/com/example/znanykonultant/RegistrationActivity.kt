package com.example.znanykonultant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.example.znanykonultant.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun showUserFragment(view: View) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.registrationFragment, UserRegisterFragment())
        transaction.commit()
    }

    fun showConsultantFragment(view: View) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.registrationFragment, KonsultantRegisterFragment())
        transaction.commit()
    }

    fun goBack(view: View){
        finish()
    }
}