package com.example.znanykonultant.consultant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.znanykonultant.R
import com.example.znanykonultant.chat.ChatsFragment
import com.example.znanykonultant.consultant.appointments.ConsultantAppointmentsFragment
import com.example.znanykonultant.consultant.profile.ConsultantProfileFragment
import com.example.znanykonultant.consultant.profile.VisitorsChartFragment
import com.example.znanykonultant.consultant.services.ConsultantServicesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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
                else -> Log.e("famousConsultant", "consultant navigation, Unknown fragment id = ${it.itemId}")
            }
            true
        }
    }
}
