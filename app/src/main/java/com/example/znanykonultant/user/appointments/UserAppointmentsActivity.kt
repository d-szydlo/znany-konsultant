package com.example.znanykonultant.user.appointments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.appointments.CommonFunctions
import com.example.znanykonultant.appointments.OccupiedTermsAdapter
import com.example.znanykonultant.dao.AppointmentsDAO
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.User
import com.example.znanykonultant.entity.WorkDays
import com.example.znanykonultant.tools.DateTimeConverter
import com.example.znanykonultant.tools.DateTimeWidgets
import com.example.znanykonultant.tools.DayOfWeekConverter
import com.example.znanykonultant.tools.TimestampConverter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UserAppointmentsActivity : AppCompatActivity() {

    var NOTIFICATION_ID = 100
    var consultant : Consultant? = null
    var user : User? = null
    var appointments : MutableList<Appointments> = mutableListOf()
    private val pattern : String = "dd.MM.yyyy HH:mm"
    private val database = Firebase.database
    private val consultantRef = database.getReference("consultants")
    private val userRef = database.getReference("users")
    private val appointmentsRef = database.getReference("appointments")
    private val userUID = FirebaseAuth.getInstance().uid
    private var consultantUID : String = ""

    private var dayOfWeek : String = ""
    private var pickedDay : MutableList<WorkDays> = mutableListOf()
    private var terms : HashMap<String, MutableList<WorkDays>> = hashMapOf()

    lateinit var name: TextView
    lateinit var surname: TextView

    private lateinit var dropdown : Spinner
    private lateinit var listAdapter: OccupiedTermsAdapter // terms
    private lateinit var listAdapter2: OccupiedTermsAdapter // work hours


    private val f = CommonFunctions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        setContentView(R.layout.activity_user_appointments)
        name = findViewById(R.id.appointmentsUserName)
        surname = findViewById(R.id.appointmentsUserSurname)
        listAdapter = OccupiedTermsAdapter(mutableListOf())
        listAdapter2 = OccupiedTermsAdapter(mutableListOf())
        initRecycler()

        getData()
        initDialog()
    }

    private fun initRecycler(){
        val recyclerView = findViewById<RecyclerView>(R.id.consultantDatesRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = listAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val recyclerView2 = findViewById<RecyclerView>(R.id.workHoursRecycler)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView2.adapter = listAdapter2
        recyclerView2.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
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

        val appointmentListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                appointments = mutableListOf()
                for (appoint in consultant!!.appointments) {
                    val appointment = dataSnapshot.child(appoint.key).getValue(Appointments::class.java)

                    if (appointment != null) {
                        appointments.add(appointment)
                    }
                    Log.e("firebase", appointment.toString())
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }


        consultantRef.addValueEventListener(postListener)
        userRef.addValueEventListener(userListener)
        appointmentsRef.addValueEventListener(appointmentListener)

    }

    private fun initDialog() {
        val date : EditText = findViewById(R.id.editTextDate)
        DateTimeWidgets(this, date).initDate()
        DateTimeWidgets(this, findViewById(R.id.appointmentTimeStart)).initTime()
        DateTimeWidgets(this, findViewById(R.id.appointmentTimeStop)).initTime()

        date.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                checkDate()
            }
        })
    }

    private fun initDropDown() {
        dropdown = findViewById(R.id.typeSpinner)
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

    private fun checkDate() {

        val noWork = findViewById<TextView>(R.id.workHoursUsr)
        val noTerms = findViewById<TextView>(R.id.termsUser)
        val date = findViewById<EditText>(R.id.editTextDate).text.toString()
        if (date != "") {
            terms = f.calculateTerms(appointments)
            val newDate = SimpleDateFormat("dd.MM.yyyy").parse(date).time
            val c = Calendar.getInstance()
            c.timeInMillis = newDate

            dayOfWeek = DayOfWeekConverter(c.get(Calendar.DAY_OF_WEEK)).convert()!!

            val timetable: Map<String, WorkDays> = consultant!!.worktime
            val days = timetable.filter {it.value.day == dayOfWeek}

            noWork.text = getString(R.string.work_hours)
            noTerms.text = getString(R.string.occupied_hours)

            if (days.isNotEmpty()) {
                pickedDay= mutableListOf()
                days.forEach {pickedDay.add(it.value)}
                if (terms.containsKey(date)) {
                    listAdapter.updateData(f.printTermsHours(date, terms))
                    listAdapter2.updateData(pickedDay)
                } else {
                    listAdapter2.updateData(pickedDay)
                    noTerms.text = ""
                }
            } else {
                noWork.text = "Nie pracujemy w ten dzień :("
            }
        }
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

        val timetable: Map<String, WorkDays> = consultant!!.worktime
        val days = timetable.filter {it.value.day == dayOfWeek}

        if(days.isNotEmpty()) {
            if(timeStart <= timeStop) {
                if (f.checkIfPossible(timeStart, timeStop, date, terms, pickedDay)) {
                    dao.addAppointment(
                        userUID!!,
                        consultantUID,
                        TimestampConverter("$date $timeStart", pattern).convert(),
                        TimestampConverter("$date $timeStop", pattern).convert(),
                        "",
                        user!!.getFullName(),
                        consultant!!.getFullName(),
                        dropdown.selectedItem.toString()
                    )
                    finish()
                    Toast.makeText(this, "Zapis udany!", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(this, "Poza dostępnymi terminami!", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "Nieprawidłowe godziny!", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this, "W ten dzień nie pracujemy!", Toast.LENGTH_SHORT).show()

        val desc = "Konsultacja z " + name.text.toString() + " " + surname.text.toString()
        setAlarm(TimestampConverter("$date $timeStartForAlarm", pattern).convert(), "Konsultacja", desc)
    }

    private fun setAlarm(time: Long, name: String, description: String){
        val intent = Intent(this, NotificationContent::class.java)
        intent.putExtra("name", name)
        intent.putExtra("description", description)
        val pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun createNotificationChannel() {
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