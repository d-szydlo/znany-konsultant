package com.example.znanykonultant.user.appointments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.example.znanykonultant.R
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
    private var terms : MutableMap<String, MutableList<WorkDays>> = mutableMapOf()

    lateinit var name: TextView
    lateinit var surname: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun checkDate() {

        val date = findViewById<EditText>(R.id.editTextDate).text.toString()
        if (date != "") {
            val debug = findViewById<TextView>(R.id.debugTextView)
            terms = calculateTerms()
            val newDate = SimpleDateFormat("dd.MM.yyyy").parse(date).time
            val c = Calendar.getInstance()
            c.timeInMillis = newDate

            dayOfWeek = DayOfWeekConverter(c.get(Calendar.DAY_OF_WEEK)).convert()!!

            val timetable: Map<String, WorkDays> = consultant!!.worktime
            val days = timetable.filter {it.value.day == dayOfWeek}

            if (days.isNotEmpty()) {
                pickedDay= mutableListOf()
                days.forEach {pickedDay.add(it.value)}
                debug.text = pickedDay.toString()
                if (terms.containsKey(date)) {
                    debug.text = " Godziny pracy: ${convertWorkHours()}\n" +
                            "Zajęte terminy: ${printTermsHours(date)}"
                } else {
                    debug.text = " Godziny pracy: ${convertWorkHours()}\n" +
                            "Dzień wolny!"
                }
            } else {
                debug.text = "Nie pracujemy w ten dzień :("
            }
        }


    }
    fun signIn(view: View) {
        val dao =  AppointmentsDAO()
        val date = findViewById<EditText>(R.id.editTextDate).text.toString()

        val timeStart = findViewById<EditText>(R.id.appointmentTimeStart).text.toString()
        val timeStop = findViewById<EditText>(R.id.appointmentTimeStop).text.toString()

        val timetable: Map<String, WorkDays> = consultant!!.worktime
        val days = timetable.filter {it.value.day == dayOfWeek}

        if(days.isNotEmpty()) {
            if(timeStart <= timeStop) {
                if (checkIfPossible(timeStart, timeStop, date)) {
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
                    Toast.makeText(this, "Zapis udany!", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(this, "Poza dostępnymi terminami!", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "Nieprawidłowe godziny!", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this, "W ten dzień nie pracujemy!", Toast.LENGTH_SHORT).show()

    }

    private fun calculateTerms() : MutableMap<String, MutableList<WorkDays>> {
        val output : MutableMap<String, MutableList<WorkDays>> = mutableMapOf()
        for (app in appointments) {
            val dateStart = DateTimeConverter(app.timestampStart).splitConverted()
            val dateStop = DateTimeConverter(app.timestampStop).splitConverted()

            if(output.containsKey(dateStart[0]))
                output[dateStart[0]]?.add(WorkDays("",dateStart[1], dateStop[1]))
            else
                output[dateStart[0]] = mutableListOf(WorkDays("", dateStart[1], dateStop[1]))
        }

        return output

    }

    private fun printTermsHours(date : String) : String {
        var output = ""
        for (value in terms[date]!!) {
            output += "${value.start} - ${value.stop}\n"
        }
        return output
    }

    private fun convertWorkHours() : String {
        var output = ""
        for( value in pickedDay) {
            output +=  "${value.start} - ${value.stop}\n"
        }
        return output
    }

    private fun checkIfPossible(timeStart : String, timeStop : String, date : String) : Boolean {
        if(terms.containsKey(date)) {
            for (value in terms[date]!!) {
                /* IF:
                    1 -> timestart, timestop in <value.start, value.stop>
                    2 -> timestop in <value.start, value.stop> & timestart < value.start
                    3 -> timestart in <value.start, value.stop> & timestop > value.stop
                 */
                if ((value.start <= timeStart && timeStop <= value.stop) ||
                    (value.start > timeStart && timeStop >= value.start) ||
                    (timeStart < value.stop && timeStop > value.stop)
                ) { return false }

            }
        }

        for (value in pickedDay) {
            if (value.start <= timeStart && timeStop <= value.stop)
                return true
        }
        return false
    }

}