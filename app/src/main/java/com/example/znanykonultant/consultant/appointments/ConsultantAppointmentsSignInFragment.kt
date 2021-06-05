package com.example.znanykonultant.consultant.appointments

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R
import com.example.znanykonultant.appointments.CommonFunctions
import com.example.znanykonultant.appointments.OccupiedTermsAdapter
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import com.example.znanykonultant.dao.AppointmentsDAO
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.WorkDays
import com.example.znanykonultant.tools.*
import com.example.znanykonultant.user.appointments.AppointmentsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ConsultantAppointmentsSignInFragment : Fragment() {

    private lateinit var listAdapter: OccupiedTermsAdapter
    private lateinit var listAdapter2: OccupiedTermsAdapter

    private lateinit var appointment : Appointments
    private var appointmentID : String = ""

    private lateinit var name : TextView
    private lateinit var date : EditText
    private lateinit var timeStart : EditText
    private lateinit var timeStop : EditText
    private lateinit var place : TextView
    private lateinit var confirmed : TextView

    private val pattern : String = "dd.MM.yyyy HH:mm"
    private lateinit var terms : HashMap<String, MutableList<WorkDays>>

    private val database = Firebase.database
    private val consultantRef = database.getReference("consultants")

    var consultant : Consultant? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_consultant_appointments_sign_in, container, false)
        listAdapter = OccupiedTermsAdapter(mutableListOf())
        listAdapter2 = OccupiedTermsAdapter(mutableListOf())
        initRecycler(view)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        val info = FormDialogs()
        val infoBuilder = info.createDialog(view, 0)



        val positiveButtonClick = { _: DialogInterface, _: Int ->
            val dao = AppointmentsDAO()
            dao.deleteAppointment(appointmentID, appointment.personID, appointment.consultantID)
            (activity as ConsultantMainPageActivity).setFragment(ConsultantAppointmentsFragment())
        }

        val delete = FormDialogs()
        val deleteBuilder = delete.createYesNoDialog(view, 0, positiveButtonClick)

        setFragmentResultListener("data") { _, bundle ->
            getData(bundle, view)
        }

        initDatabaseListener(view)

        view.findViewById<Button>(R.id.consultantBackBtn).setOnClickListener {
            (activity as ConsultantMainPageActivity).setFragment(ConsultantAppointmentsFragment())
        }

        view.findViewById<Button>(R.id.consultantCancelBtn).setOnClickListener {
            deleteBuilder.show()
        }
        view.findViewById<Button>(R.id.consultantConfirmBtn).setOnClickListener {

            val dao = AppointmentsDAO()
            val update: MutableMap<String, Any> = HashMap()
            update["confirmed"] = true
            dao.modifyAppointment(update, appointmentID)
            (activity as ConsultantMainPageActivity).setFragment(ConsultantAppointmentsFragment())

        }
        view.findViewById<Button>(R.id.consultantUpdateBtn).setOnClickListener {

            val dao = AppointmentsDAO()
            val update: MutableMap<String, Any> = HashMap()

            val newDate = date.text.toString()
            val newStartTime = timeStart.text.toString()
            val newStopTime = timeStop.text.toString()


            if(!info.confirmedAction)
                infoBuilder.show()
            else {
                update["confirmed"] = true
                update["timestampStart"] = TimestampConverter("$newDate $newStartTime", pattern).convert()
                update["timestampStop"] = TimestampConverter("$newDate $newStopTime", pattern).convert()
                dao.modifyAppointment(update, appointmentID)
                (activity as ConsultantMainPageActivity).setFragment(ConsultantAppointmentsFragment())
            }
        }
        return view
    }

    private fun initRecycler(view: View){
        val recyclerView = view.findViewById<RecyclerView>(R.id.consultantDatesRecycler)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = listAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))

        val recyclerView2 = view.findViewById<RecyclerView>(R.id.workHoursRecycler)
        recyclerView2.layoutManager = LinearLayoutManager(view.context)
        recyclerView2.adapter = listAdapter2
        recyclerView2.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
    }

    private fun initDatabaseListener(view: View) {
        val consultantListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                consultant = dataSnapshot.child(appointment.consultantID).getValue(Consultant::class.java)
                initFields(view)
                initDialog(view)
                setData()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        consultantRef.addValueEventListener(consultantListener)
    }


    private fun initFields(view: View) {
        name = view.findViewById(R.id.appointmentsConsultantName)
        date = view.findViewById(R.id.appointmentsConsultantDate)
        place = view.findViewById(R.id.appointmentsConsultantPlace)
        confirmed = view.findViewById(R.id.appointmentsConsultantConfirmed)
        timeStart = view.findViewById(R.id.appointmentConsultantTimeStart)
        timeStop = view.findViewById(R.id.appointmentConsultantTimeStop)

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

    private fun getData(bundle: Bundle, view: View) {
        appointment = Appointments(
            bundle.getString("client", ""),
            "",
            bundle.getString("clientID", ""),
            bundle.getString("consultantID", ""),
            bundle.getLong("dateStart", 0L),
            bundle.getLong("dateStop", 0L),
            bundle.getString("place", ""),
            bundle.getString("type", ""),
            bundle.getBoolean("confirmed", false)
        )
        if(appointment.confirmed) {
            view.findViewById<Button>(R.id.consultantConfirmBtn).visibility = View.GONE
        }

        appointmentID = bundle.getString("id", "")
        terms = bundle.getSerializable("terms") as HashMap<String, MutableList<WorkDays>>
    }

    private fun setData() {
        name.text = appointment.person
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

    fun checkDate(view: View) {
        val noWork = view.findViewById<TextView>(R.id.workHoursCons)
        val noTerms = view.findViewById<TextView>(R.id.termsConsultant)

        val f = CommonFunctions()
        val dateText = date.text.toString()
        if (dateText.isNotEmpty()) {

            val newDate = SimpleDateFormat("dd.MM.yyyy").parse(dateText).time
            val c = Calendar.getInstance()
            c.timeInMillis = newDate

            val dayOfWeek = DayOfWeekConverter(c.get(Calendar.DAY_OF_WEEK)).convert()!!

            val timetable : Map<String, WorkDays> = consultant!!.worktime
            val days = timetable.filter {it.value.day == dayOfWeek}

            noWork.text = getString(R.string.work_hours)
            noTerms.text = getString(R.string.occupied_hours)
            if(days.isNotEmpty()) {
                val pickedDay : MutableList<WorkDays> = mutableListOf()
                days.forEach {pickedDay.add(it.value)}
                if (terms.containsKey(dateText)) {
                    listAdapter.updateData(f.printTermsHours(dateText, terms))
                    listAdapter2.updateData(pickedDay)
                } else {
                    listAdapter2.updateData(pickedDay)
                    noTerms.text = ""
                }
            }
            else {
                noWork.text = "Nie pracujemy w ten dzień :("
            }
        }
    }


}