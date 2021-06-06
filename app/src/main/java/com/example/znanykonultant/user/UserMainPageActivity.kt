package com.example.znanykonultant.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.znanykonultant.R
import com.example.znanykonultant.chat.ChatsFragment
import com.example.znanykonultant.login.LoginActivity
import com.example.znanykonultant.user.appointments.UserAppointmentsFragment
import com.example.znanykonultant.user.favourites.UserFavouritesFragment
import com.example.znanykonultant.user.search.FilterFragment
import com.example.znanykonultant.user.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class UserMainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main_page)

        setFragment(SearchFragment())
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
                R.id.user_nav_search -> setFragment(SearchFragment())
                R.id.user_nav_favourites -> setFragment(UserFavouritesFragment())
                R.id.user_nav_chats -> setFragment(ChatsFragment())
                R.id.user_nav_appointments -> setFragment(UserAppointmentsFragment())
                else -> Log.e("famousConsultant", "user navigation, Unknown fragment id = ${it.itemId}")
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
