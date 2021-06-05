package com.example.znanykonultant.user.appointments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import com.example.znanykonultant.R
import com.example.znanykonultant.appointments.CommonFunctions
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import com.example.znanykonultant.consultant.appointments.ConsultantAppointmentsFragment
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
import java.util.*
import kotlin.collections.HashMap

class UserAppointmentsSignInFragment : Fragment() {

    private lateinit var appointment : Appointments
    private var appointmentID : String = ""
    private val pattern : String = "dd.MM.yyyy HH:mm"
    
    private lateinit var name : TextView
    private lateinit var date : EditText
    private lateinit var timeStart : EditText
    private lateinit var timeStop : EditText
    private lateinit var place : TextView
    private lateinit var confirmed : TextView
    private lateinit var debug : TextView

    private val database = Firebase.database
    private val consultantRef = database.getReference("consultants")

    private var dayOfWeek : String = ""
    private var pickedDay : MutableList<WorkDays> = mutableListOf()
    private lateinit var terms : HashMap<String, MutableList<WorkDays>>

    var consultant : Consultant? = null
    private val f = CommonFunctions()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_user_appointments_sign_in, container, false)

        val info = FormDialogs()
        val infoBuilder = info.createDialog(view, 0)

        val positiveButtonClick = { _: DialogInterface, _: Int ->
            val dao = AppointmentsDAO()
            dao.deleteAppointment(appointmentID, appointment.personID, appointment.consultantID)
            (activity as UserMainPageActivity).setFragment(UserAppointmentsFragment())
        }

        val deleteBuilder = FormDialogs().createYesNoDialog(view, 0, positiveButtonClick)

        setFragmentResultListener("data") { _, bundle ->
            getData(bundle, view)
        }

        initDatabaseListener(view)


        view.findViewById<Button>(R.id.userBackBtn).setOnClickListener {
            (activity as UserMainPageActivity).setFragment(UserAppointmentsFragment())
        }
        view.findViewById<Button>(R.id.usrChangeBtn).setOnClickListener {
            if(!info.confirmedAction)
                infoBuilder.show()
            else {
                modifyAppointment(view)
            }
        }
        view.findViewById<Button>(R.id.usrCancelBtn).setOnClickListener {
            deleteBuilder.show()
        }

        return view
    }

    private fun initDatabaseListener(view: View) {
        val consultantListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                consultant = dataSnapshot.child(appointment.consultantID).getValue(Consultant::class.java)
                initFields(view)
                initDialog(view)
                setData()
                Log.e("firebase", consultant.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        consultantRef.addValueEventListener(consultantListener)
    }

    private fun modifyAppointment(view: View) {
        val dao = AppointmentsDAO()
        val update: MutableMap<String, Any> = HashMap()

        val newDate = date.text.toString()
        val newStartTime = timeStart.text.toString()
        val newStopTime = timeStop.text.toString()

        val timetable : Map<String, WorkDays> = consultant!!.worktime
        val days = timetable.filter {it.value.day == dayOfWeek}

        if(days.isNotEmpty()) {
            if(newStartTime <= newStopTime) {
                if (f.checkIfPossible(newStartTime, newStopTime, newDate, terms, pickedDay)) {
                    update["confirmed"] = false
                    update["timestampStart"] = TimestampConverter("$newDate $newStartTime", pattern).convert()
                    update["timestampStop"] = TimestampConverter("$newDate $newStopTime", pattern).convert()
                    dao.modifyAppointment(update, appointmentID)
                    (activity as UserMainPageActivity).setFragment(UserAppointmentsFragment())
                    Toast.makeText(view.context, "Zapis udany!", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(view.context, "Poza dostępnymi terminami!", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(view.context, "Nieprawidłowe godziny!", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(view.context, "W ten dzień nie pracujemy!", Toast.LENGTH_SHORT).show()
    }

    private fun initFields(view: View) {
        name = view.findViewById(R.id.appointmentsUserName)
        date = view.findViewById(R.id.appointmentsUserDate)
        timeStart = view.findViewById(R.id.appointmentUserTimeStart)
        timeStop = view.findViewById(R.id.appointmentUserTimeStop)
        place = view.findViewById(R.id.appointmentsUserPlace)
        confirmed = view.findViewById(R.id.appointmentsUserConfirmed)
        debug = view.findViewById(R.id.debugTextConsultantView)
    }

    private fun initDialog(view: View) {
        DateTimeWidgets(view.context, date).initDate()
        DateTimeWidgets(view.context, timeStart).initTime()
        DateTimeWidgets(view.context, timeStop).initTime()

        date.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                checkDate(view)
            }
        })
    }

    fun checkDate(view: View) {
        val dateText = date.text.toString()
        if (dateText.isNotEmpty()) {

            val newDate = SimpleDateFormat("dd.MM.yyyy").parse(dateText).time
            val c = Calendar.getInstance()
            c.timeInMillis = newDate

            dayOfWeek = DayOfWeekConverter(c.get(Calendar.DAY_OF_WEEK)).convert()!!

            val timetable : Map<String, WorkDays> = consultant!!.worktime
            val days = timetable.filter {it.value.day == dayOfWeek}

            if(days.isNotEmpty()) {
                pickedDay= mutableListOf()
                days.forEach {pickedDay.add(it.value)}
                if (terms.containsKey(dateText)) {
                    debug.text = " Godziny pracy: ${f.convertWorkHours(pickedDay)}\n" +
                            "Zajęte terminy: ${f.printTermsHours(dateText, terms)}"
                } else {
                    debug.text = " Godziny pracy: ${f.convertWorkHours(pickedDay)}\n" +
                            "Dzień wolny!"
                }
            }
            else {
                debug.text = "Nie pracujemy w ten dzień :("
            }
        }
    }

    private fun getData(bundle: Bundle, view: View) {
        appointment = Appointments("",
            bundle.getString("consultant", ""),
            bundle.getString("clientID", ""),
            bundle.getString("consultantID", ""),
            bundle.getLong("dateStart", 0L),
            bundle.getLong("dateStop", 0L),
            bundle.getString("place", ""),
            bundle.getBoolean("confirmed", false),
        )
        if(!appointment.confirmed) {
            view.findViewById<Button>(R.id.usrChangeBtn).visibility = View.GONE
        }
        appointmentID = bundle.getString("id", "")
        terms = bundle.getSerializable("terms") as HashMap<String, MutableList<WorkDays>>


    }

    private fun setData() {
        name.text = appointment.consultant
        place.text = appointment.place

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
}