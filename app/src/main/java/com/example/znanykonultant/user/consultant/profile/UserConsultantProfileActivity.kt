package com.example.znanykonultant.user.consultant.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.znanykonultant.R
import com.example.znanykonultant.user.consultant.profile.opinion.OpinionActivity

class UserConsultantProfileActivity : AppCompatActivity() {
    lateinit var consultantUid : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_consultant_profile)
        consultantUid = this.intent.getStringExtra("consultant_uid").toString()

        val newProfileFragment: UserConsultantProfileFragment = UserConsultantProfileFragment(consultantUid) // TODO przechowywanie uid przy obrocie, lub zablokowanie obrotu
        val newOpinionFragment: UserConsultantOpinionFragment = UserConsultantOpinionFragment(consultantUid)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerProfil, newProfileFragment)
        transaction.replace(R.id.fragmentContainerOpinions, newOpinionFragment)
        transaction.commit()
    }

    fun makeAnAppointment(view: View) {}

    fun addOpinion(view: View) {
        val myIntent = Intent(this, OpinionActivity::class.java)
        myIntent.putExtra("consultant_uid",consultantUid)
        startActivity(myIntent)
    }

    fun startChatting(view: View) {}
}