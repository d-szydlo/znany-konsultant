package com.example.znanykonultant.user.consultant.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.znanykonultant.R

class UserConsultantProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_consultant_profile)
        var uid = this.intent.getStringExtra("consultant_uid")
        var newFragment: UserConsultantProfileFragment = UserConsultantProfileFragment(uid)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerProfil, newFragment)
        transaction.commit()
    }

    private fun setFragment(newFragment: UserConsultantProfileFragment) {

    }


}