package com.example.znanykonultant.consultant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.znanykonultant.R
import com.example.znanykonultant.chat.ChatsFragment
import com.example.znanykonultant.consultant.appointments.ConsultantAppointmentsFragment
import com.example.znanykonultant.consultant.profile.ConsultantProfileFragment
import com.example.znanykonultant.consultant.services.ConsultantServicesFragment
import com.example.znanykonultant.consultant.workingHours.ConsultantWorkingHoursFragment
import com.example.znanykonultant.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class ConsultantMainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultant_main_page)

        setFragment(ConsultantProfileFragment())
        prepareNavigation()
    }

    fun setFragment(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.commit()
    }

    private fun prepareNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.consultant_nav_profile -> setFragment(ConsultantProfileFragment())
                R.id.consultant_nav_chats -> setFragment(ChatsFragment())
                R.id.consultant_nav_appointments -> setFragment(ConsultantAppointmentsFragment())
                R.id.consultant_nav_services -> setFragment(ConsultantServicesFragment())
                R.id.consultant_nav_working_hours -> setFragment(ConsultantWorkingHoursFragment())
                else -> Log.e("famousConsultant", "consultant navigation, Unknown fragment id = ${it.itemId}")
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
