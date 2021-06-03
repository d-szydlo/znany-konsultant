package com.example.znanykonultant.user.consultant.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.znanykonultant.R
import com.example.znanykonultant.chat.ChatsFragment
import com.example.znanykonultant.chat.SingleChatActivity
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.user.appointments.UserAppointmentsActivity
import com.example.znanykonultant.user.consultant.profile.opinion.OpinionActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

    fun makeAnAppointment(view: View) {
        val myIntent = Intent(this, UserAppointmentsActivity::class.java)
        myIntent.putExtra("consultant_uid",consultantUid)
        startActivity(myIntent)
    }

    fun addOpinion(view: View) {
        val myIntent = Intent(this, OpinionActivity::class.java)
        myIntent.putExtra("consultant_uid",consultantUid)
        startActivity(myIntent)
    }

    fun startChatting(view: View) {
        val intent = Intent(this, SingleChatActivity::class.java)
        intent.putExtra(ChatsFragment.NAME_KEY, getConsultantName(consultantUid))
        intent.putExtra(ChatsFragment.UID_KEY, consultantUid)
        startActivity(intent)
    }

    private fun getConsultantName(uid: String) : String {
        var name = ""
        val consultantRef = FirebaseDatabase.getInstance().getReference("consultants/${uid}")

        consultantRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val consultant = snapshot.getValue(Consultant::class.java) ?: return
                name = "${consultant.name} ${consultant.surname}"
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return name
    }
}