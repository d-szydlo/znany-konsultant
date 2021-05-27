package com.example.znanykonultant.user.appointments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.setFragmentResultListener
import com.example.znanykonultant.R
import com.example.znanykonultant.dao.AppointmentsDAO
import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.user.UserMainPageActivity

class UserAppointmentsSignInFragment : Fragment() {

    private lateinit var appointment : Appointments
    private var appointmentID : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_user_appointments_sign_in, container, false)
        setFragmentResultListener("data") { _, bundle ->
            getData(bundle)
        }

        view.findViewById<Button>(R.id.userBackBtn).setOnClickListener {
            (activity as UserMainPageActivity).setFragment(UserAppointmentsFragment())
        }
        view.findViewById<Button>(R.id.usrCancelBtn).setOnClickListener {

            val dao = AppointmentsDAO()

            dao.deleteAppointment(appointmentID)
            (activity as UserMainPageActivity).setFragment(UserAppointmentsFragment())
        }

        return view
    }

    private fun getData(bundle: Bundle) {
        appointment = Appointments("",
            bundle.getString("consultant", ""),
            bundle.getLong("date", 0L),
            bundle.getString("place", "")
        )
        appointmentID = bundle.getString("id", "")

        Log.e("my_fragment", appointment.toString())
    }

}