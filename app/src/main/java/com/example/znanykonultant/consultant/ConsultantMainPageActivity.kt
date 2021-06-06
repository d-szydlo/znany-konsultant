package com.example.znanykonultant.consultant

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.znanykonultant.R
import com.example.znanykonultant.chat.ChatsFragment
import com.example.znanykonultant.consultant.appointments.ConsultantAppointmentsFragment
import com.example.znanykonultant.consultant.profile.ConsultantProfileFragment
import com.example.znanykonultant.consultant.services.ConsultantServicesFragment
import com.example.znanykonultant.consultant.workingHours.ConsultantWorkingHoursFragment
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ConsultantMainPageActivity : AppCompatActivity() {

    val userUid = FirebaseAuth.getInstance().uid
    val database = Firebase.database
    val consultantRef = database.getReference("consultants")
    private val appointmentsRef = database.getReference("appointments")
    var appointments : MutableList<Appointments> = mutableListOf()
    private var appointmentsIds : MutableList<String> = mutableListOf()
    var data : Appointments? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultant_main_page)

        setFragment(ConsultantProfileFragment())
        prepareNavigation()
        checkUnconfirmedAppointments()
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

    private fun checkUnconfirmedAppointments(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                appointmentsIds = mutableListOf()
                dataSnapshot.child(userUid!!).child("appointments").children.mapNotNullTo(appointmentsIds)
                {
                    it.key
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }

        val appointmentListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                appointments = mutableListOf()
                for (key in appointmentsIds) {
                    val appointment = dataSnapshot.child(key).getValue(Appointments::class.java)
                    if (appointment != null) {
                        appointments.add(appointment)
                    }
                }
                if (appointments.size > 0){
                    appointments.forEach {
                        if(!it.confirmed){
                            send()
                            return
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }

        consultantRef.addValueEventListener(postListener)
        appointmentsRef.addValueEventListener(appointmentListener)
    }

    private fun send(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = NotificationChannel("reminder2", "unconfirmedAppointments", NotificationManager.IMPORTANCE_DEFAULT)
            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            val builder = Notification.Builder(this, "reminder2", )
                    .setContentTitle("Niepotwierdzone spotkania")
                    .setContentText("Masz niepotwierdzone spotkania, wejdź w zakładkę Spotkania aby je potwierdzić lub anulować")
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.bell)

            val intent = Intent(this, ConsultantMainPageActivity::class.java)
            val pending = PendingIntent.getActivity(this, 0, intent, 0)
            builder.setContentIntent(pending)

            val notification = builder.build()
            manager.notify(1234, notification)
        }
    }
}
