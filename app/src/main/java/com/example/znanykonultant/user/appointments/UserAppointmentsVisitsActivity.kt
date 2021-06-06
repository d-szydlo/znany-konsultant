package com.example.znanykonultant.user.appointments

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.appointments.CommonFunctions
import com.example.znanykonultant.appointments.OccupiedTermsAdapter
import com.example.znanykonultant.dao.AppointmentsDAO
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.WorkDays
import com.example.znanykonultant.tools.*
import com.example.znanykonultant.user.UserMainPageActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class UserAppointmentsVisitsActivity : AppCompatActivity() {

    private lateinit var listAdapter: OccupiedTermsAdapter // terms
    private lateinit var listAdapter2: OccupiedTermsAdapter // work hours

    private lateinit var appointment : Appointments
    private var appointmentID : String = ""
    private val pattern : String = "dd.MM.yyyy HH:mm"

    private lateinit var name : TextView
    private lateinit var date : EditText
    private lateinit var timeStart : EditText
    private lateinit var timeStop : EditText
    private lateinit var place : TextView
    private lateinit var confirmed : TextView
    private lateinit var noWork : TextView
    private lateinit var noTerms : TextView

    private val database = Firebase.database
    private val consultantRef = database.getReference("consultants")

    private var dayOfWeek : String = ""
    private var pickedDay : MutableList<WorkDays> = mutableListOf()
    private lateinit var terms : HashMap<String, MutableList<WorkDays>>

    var consultant : Consultant? = null
    private lateinit var f : CommonFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_appointments_visits)

        listAdapter = OccupiedTermsAdapter(mutableListOf())
        listAdapter2 = OccupiedTermsAdapter(mutableListOf())
        initRecycler()

        if(intent != null) {
            val bundle = intent.getBundleExtra("data")
            getData(bundle!!)
        }

        initDatabaseListener()
        f = CommonFunctions()
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

    private fun initDatabaseListener() {
        val consultantListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                consultant = dataSnapshot.child(appointment.consultantID).getValue(Consultant::class.java)
                initFields()
                initDialog()
                setData()
                Log.e("firebase", consultant.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        consultantRef.addValueEventListener(consultantListener)
    }


    private fun initFields() {
        name = findViewById(R.id.appointmentsUserName)
        date = findViewById(R.id.appointmentsUserDate)
        timeStart = findViewById(R.id.appointmentUserTimeStart)
        timeStop = findViewById(R.id.appointmentUserTimeStop)
        place = findViewById(R.id.appointmentsUserPlace)
        confirmed = findViewById(R.id.appointmentsUserConfirmed)
        noWork = findViewById(R.id.workHoursUsr)
        noTerms = findViewById(R.id.termsUser)
    }

    private fun initDialog() {
        DateTimeWidgets(this, date).initDate()
        DateTimeWidgets(this, timeStart).initTime()
        DateTimeWidgets(this, timeStop).initTime()
        date.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                checkDate()
            }
        })
    }

    private fun modifyAppointment() {
        val dao = AppointmentsDAO()
        val update: MutableMap<String, Any> = HashMap()

        val newDate = date.text.toString()
        val newStartTime = timeStart.text.toString()
        val newStopTime = timeStop.text.toString()

        val currDate = TimestampConverter(
            LocalDateTime.now().format((DateTimeFormatter.ofPattern(pattern))),
            pattern).convert()
        val checkDate = TimestampConverter("$newDate $newStartTime", pattern).convert()

        if(newDate.isNotEmpty() && newStartTime.isNotEmpty() && newStopTime.isNotEmpty()) {
            if (currDate < checkDate) {
                val timetable: Map<String, WorkDays> = consultant!!.worktime
                val days = timetable.filter { it.value.day == dayOfWeek }

                if (days.isNotEmpty()) {
                    if (newStartTime <= newStopTime) {
                        if (f.checkIfPossible(
                                newStartTime,
                                newStopTime,
                                newDate,
                                terms,
                                pickedDay
                            )
                        ) {
                            update["confirmed"] = false
                            update["timestampStart"] =
                                TimestampConverter("$newDate $newStartTime", pattern).convert()
                            update["timestampStop"] =
                                TimestampConverter("$newDate $newStopTime", pattern).convert()
                            dao.modifyAppointment(update, appointmentID)
                            Toast.makeText(this, "Zapis udany!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else
                            Toast.makeText(
                                this,
                                "Poza dostępnymi terminami!",
                                Toast.LENGTH_SHORT
                            ).show()
                    } else
                        Toast.makeText(this, "Nieprawidłowe godziny!", Toast.LENGTH_SHORT)
                            .show()
                } else
                    Toast.makeText(this, "W ten dzień nie pracujemy!", Toast.LENGTH_SHORT)
                        .show()
            } else {
                Toast.makeText(this, "Wydarzenie w przeszłości!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Wypełnij pola!", Toast.LENGTH_SHORT)
                .show()
        }
    }


    fun checkDate() {
        val dateText = date.text.toString()
        if (dateText.isNotEmpty()) {

            val newDate = SimpleDateFormat("dd.MM.yyyy").parse(dateText).time
            val c = Calendar.getInstance()
            c.timeInMillis = newDate

            dayOfWeek = DayOfWeekConverter(c.get(Calendar.DAY_OF_WEEK)).convert()!!

            val timetable : Map<String, WorkDays> = consultant!!.worktime
            val days = timetable.filter {it.value.day == dayOfWeek}

            noWork.text = getString(R.string.work_hours)
            noTerms.text = getString(R.string.occupied_hours)

            if(days.isNotEmpty()) {
                pickedDay = mutableListOf()
                days.forEach {pickedDay.add(it.value)}
                if (terms.containsKey(dateText)) {
                    // print occupied hours

                    listAdapter.updateData(f.printTermsHours(dateText, terms))
                    // print work hours
                    listAdapter2.updateData(pickedDay)
                } else {
                    listAdapter2.updateData(pickedDay)
                    listAdapter.updateData(mutableListOf())
                    noTerms.text = ""
                }
            }
            else {
                noWork.text = "Nie pracujemy w ten dzień :("
            }
        }
    }

    private fun getData(bundle: Bundle) {
        appointment = Appointments(bundle.getString("id",""),
            "",
            bundle.getString("consultant", ""),
            bundle.getString("clientID", ""),
            bundle.getString("consultantID", ""),
            bundle.getLong("dateStart", 0L),
            bundle.getLong("dateStop", 0L),
            bundle.getString("place", ""),
            bundle.getString("type", ""),
            bundle.getBoolean("confirmed", false),
        )
        if(!appointment.confirmed) {
            findViewById<Button>(R.id.usrChangeBtn).visibility = View.GONE
        }
        appointmentID = bundle.getString("id", "")
        terms = bundle.getSerializable("terms") as HashMap<String, MutableList<WorkDays>>


    }

    private fun setData() {
        name.text = appointment.consultant
        place.text = appointment.type

        if(appointment.confirmed)
            confirmed.text = "Tak"
        else
            confirmed.text = "Nie"

        val dateStart = DateTimeConverter(appointment.timestampStart).splitConverted()
        val dateStop = DateTimeConverter(appointment.timestampStop).splitConverted()

        date.setText(dateStart[0])
        timeStart.setText(dateStart[1])
        timeStop.setText(dateStop[1])
    }

    fun deleteUsr(view: View) {
        val positiveButtonClick = { _: DialogInterface, _: Int ->
            val dao = AppointmentsDAO()
            dao.deleteAppointment(appointmentID, appointment.personID, appointment.consultantID)
            finish()
        }
        val delete = FormDialogs()
        val deleteBuilder = delete.createYesNoDialog(this, 0, positiveButtonClick)

        deleteBuilder.show()

    }
    fun goBackUsr(view: View) {
        finish()
    }

    fun modifyUsr(view: View) {
        modifyAppointment()
    }
}