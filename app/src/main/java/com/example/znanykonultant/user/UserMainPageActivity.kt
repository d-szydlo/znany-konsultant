package com.example.znanykonultant.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.znanykonultant.R
import com.example.znanykonultant.chat.ChatsFragment
import com.example.znanykonultant.user.appointments.UserAppointmentsFragment
import com.example.znanykonultant.user.favourites.UserFavouritesFragment
import com.example.znanykonultant.user.search.UserSearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserMainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main_page)

        setFragment(UserSearchFragment())
        prepareNavigation()
    }

    private fun setFragment(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.commit()
    }

    private fun prepareNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.user_nav_search -> setFragment(UserSearchFragment())
                R.id.user_nav_favourites -> setFragment(UserFavouritesFragment())
                R.id.user_nav_chats -> setFragment(ChatsFragment())
                R.id.user_nav_appointments -> setFragment(UserAppointmentsFragment())
                else -> Log.e("famousConsultant", "user navigation, Unknown fragment id = ${it.itemId}")
            }
            true
        }
    }
}
