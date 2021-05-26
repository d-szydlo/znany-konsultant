package com.example.znanykonultant.user.consultant.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.znanykonultant.R

class UserConsultantProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_consultant_profile)
        var uid = this.intent.getStringExtra("consultant_uid")
        var newFragment: UserConsultantProfileFragment = UserConsultantProfileFragment(uid) // TODO przechowywanie uid przy obrocie, lub zablokowanie obrotu
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerProfil, newFragment)
        transaction.commit()
    }

    fun makeAnAppointment(view: View) {}
    fun addOpinion(view: View) {}
    fun startChatting(view: View) {}
}