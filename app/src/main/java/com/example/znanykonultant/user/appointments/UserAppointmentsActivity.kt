package com.example.znanykonultant.user.appointments

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.znanykonultant.R
import com.example.znanykonultant.dao.AppointmentsDAO
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.User
import com.example.znanykonultant.tools.DateTimeWidgets
import com.example.znanykonultant.tools.TimestampConverter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserAppointmentsActivity : AppCompatActivity() {

    var NOTIFICATION_ID = 100
    var consultant : Consultant? = null
    var user : User? = null
    private val pattern : String = "dd.MM.yyyy HH:mm"
    private val database = Firebase.database
    private val consultantRef = database.getReference("consultants")
    private val userRef = database.getReference("users")
    private val userUID = FirebaseAuth.getInstance().uid
    private var consultantUID : String = ""

    lateinit var name: TextView
    lateinit var surname: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        setContentView(R.layout.activity_user_appointments)
        name = findViewById(R.id.appointmentsUserName)
        surname = findViewById(R.id.appointmentsUserSurname)

        getData()
        initDialog()
    }

    private fun getData() {

        consultantUID = this.intent.getStringExtra("consultant_uid").toString()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                consultant = dataSnapshot.child(consultantUID).getValue(Consultant::class.java)
                setInfo()
                initDropDown()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        }
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = dataSnapshot.child(userUID!!).getValue(User::class.java)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        consultantRef.addValueEventListener(postListener)
        userRef.addValueEventListener(userListener)

    }

    private fun initDialog() {
        DateTimeWidgets(this, findViewById(R.id.editTextDate)).initDate()
        DateTimeWidgets(this, findViewById(R.id.appointmentTimeStart)).initTime()
        DateTimeWidgets(this, findViewById(R.id.appointmentTimeStop)).initTime()
    }

    private fun initDropDown() {
        val dropdown = findViewById<Spinner>(R.id.typeSpinner)
        consultant?.consultantService
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, consultant!!.getAllServiceNames())
        dropdown.adapter = adapter
    }

    private fun setInfo() {
        name.text = consultant?.name
        surname.text = consultant?.surname
    }

    fun goBack(view: View) {
        finish()
    }
    fun signIn(view: View) {
        val dao =  AppointmentsDAO()
        val date = findViewById<EditText>(R.id.editTextDate).text.toString()

        val timeStart = findViewById<EditText>(R.id.appointmentTimeStart).text.toString()

        val timeStartSplited = timeStart.split(":")
        val timeStartMinusOneHour: Int
        timeStartMinusOneHour = if (timeStartSplited[0] == "00"){
            23
        }
        else{
            timeStartSplited[0].toInt() - 1
        }
        val timeStartForAlarm = timeStartMinusOneHour.toString() + ":" + timeStartSplited[1]


        val timeStop = findViewById<EditText>(R.id.appointmentTimeStop).text.toString()
        dao.addAppointment(
            userUID!!,
            consultantUID,
            TimestampConverter("$date $timeStart", pattern).convert(),
            TimestampConverter("$date $timeStop", pattern).convert(),
            "",
            user!!.getFullName(),
            consultant!!.getFullName()
        )
        finish()
        val desc = "Konsultacja z " + name.text.toString() + " " + surname.text.toString()
        setAlarm(TimestampConverter("$date $timeStartForAlarm", pattern).convert(), "Konsultacja", desc)
    }

    fun setAlarm(time: Long, name: String, description: String){
        val intent = Intent(this, NotificationContent::class.java)
        intent.putExtra("name", name)
        intent.putExtra("description", description)
        val pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var name: String = "reminderChannel"
            var description: String = "Channel for reminders"
            var importance: Int = NotificationManager.IMPORTANCE_DEFAULT
            var channel: NotificationChannel = NotificationChannel("reminder", name, importance)
            channel.description = description

            var notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}