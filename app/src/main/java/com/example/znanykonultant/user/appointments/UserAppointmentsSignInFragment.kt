package com.example.znanykonultant.user.appointments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import com.example.znanykonultant.R
import com.example.znanykonultant.consultant.ConsultantMainPageActivity
import com.example.znanykonultant.consultant.appointments.ConsultantAppointmentsFragment
import com.example.znanykonultant.dao.AppointmentsDAO
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.tools.DateTimeConverter
import com.example.znanykonultant.tools.DateTimeWidgets
import com.example.znanykonultant.tools.FormDialogs
import com.example.znanykonultant.tools.TimestampConverter
import com.example.znanykonultant.user.UserMainPageActivity
import java.util.*

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_user_appointments_sign_in, container, false)
        initFields(view)
        initDialog(view)
        val info = FormDialogs()
        val infoBuilder = info.createDialog(view, 0)

        val positiveButtonClick = { _: DialogInterface, _: Int ->
            val dao = AppointmentsDAO()
            dao.deleteAppointment(appointmentID, appointment.personID, appointment.consultantID)
            (activity as UserMainPageActivity).setFragment(UserAppointmentsFragment())
        }

        val delete = FormDialogs()
        val deleteBuilder = info.createYesNoDialog(view, 0, positiveButtonClick)

        setFragmentResultListener("data") { _, bundle ->
            getData(bundle, view)
        }

        view.findViewById<Button>(R.id.userBackBtn).setOnClickListener {
            (activity as UserMainPageActivity).setFragment(UserAppointmentsFragment())
        }
        view.findViewById<Button>(R.id.usrChangeBtn).setOnClickListener {
            val dao = AppointmentsDAO()
            val update: MutableMap<String, Any> = HashMap()

            val newDate = date.text.toString()
            val newStartTime = timeStart.text.toString()
            val newStopTime = timeStop.text.toString()


            if(!info.confirmedAction)
                infoBuilder.show()
            else {
                update["confirmed"] = false
                update["timestampStart"] = TimestampConverter("$newDate $newStartTime", pattern).convert()
                update["timestampStop"] = TimestampConverter("$newDate $newStopTime", pattern).convert()
                dao.modifyAppointment(update, appointmentID)
                (activity as UserMainPageActivity).setFragment(UserAppointmentsFragment())
            }

        }
        view.findViewById<Button>(R.id.usrCancelBtn).setOnClickListener {
            deleteBuilder.show()
        }

        return view
    }

    private fun initFields(view: View) {
        name = view.findViewById(R.id.appointmentsUserName)
        date = view.findViewById(R.id.appointmentsUserDate)
        timeStart = view.findViewById(R.id.appointmentUserTimeStart)
        timeStop = view.findViewById(R.id.appointmentUserTimeStop)
        place = view.findViewById(R.id.appointmentsUserPlace)
        confirmed = view.findViewById(R.id.appointmentsUserConfirmed)
    }

    private fun initDialog(view: View) {
        DateTimeWidgets(view.context, date).initDate()
        DateTimeWidgets(view.context, timeStart).initTime()
        DateTimeWidgets(view.context, timeStop).initTime()
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
        setData()
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