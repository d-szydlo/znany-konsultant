package com.example.znanykonultant.consultant.workingHours

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.znanykonultant.R

class ConsultantWorkingHoursFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_consultant_working_hours, container, false)
        view.findViewById<Button>(R.id.add_working_hours_button).setOnClickListener { addNewHours() }
        return view
    }

    private fun addNewHours(){
        val intent = Intent(activity, ConsultantAddWorkingHoursActivity::class.java)
        startActivity(intent)
    }
}
