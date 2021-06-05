package com.example.znanykonultant.consultant.workingHours

import android.app.AlertDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.znanykonultant.R
import com.example.znanykonultant.databinding.ActivityConsultantAddWorkingHoursBinding
import com.example.znanykonultant.entity.WorkDays
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ConsultantAddWorkingHoursActivity : AppCompatActivity() {
    private lateinit var binding : ActivityConsultantAddWorkingHoursBinding

    private var startHour = "08:00"
    private var endHour = "16:00"
    private var dayPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultantAddWorkingHoursBinding.inflate(layoutInflater)
        val view = binding.root

        showWorkingHourInfo()
        setContentView(view)
    }

    private fun showWorkingHourInfo() {
        binding.dayInfo.text = "${getString(R.string.day_with_colon)} ${weekDays[dayPosition]}"
        binding.startHourInfo.text = "${getString(R.string.start_with_colon)} $startHour"
        binding.endHourInfo.text = "${getString(R.string.stop_with_colon)} $endHour"
    }

    fun showDayPickerDialog(view: View) {
        val builder = AlertDialog.Builder(this)
        var tempId = 0

        builder.setTitle("Wybierz dzień tygodnia")

        builder.setSingleChoiceItems(weekDays, 0) { _, which -> tempId = which }

        builder.setPositiveButton("OK") { _, _ ->
            dayPosition = tempId
            showWorkingHourInfo()
        }

        builder.create().show()
    }

    fun showBeginHourPickerDialog(view: View) {
        val hour = startHour.substringBefore(":").toInt()
        val minute = startHour.substringAfter(":").toInt()

        TimePickerDialog(this, { _, newHour, newMinute ->
            val newHourString = if (newHour < 10)  "0${newHour}" else "$newHour"
            val newMinuteString = if (newMinute < 10)  "0${newMinute}" else "$newMinute"

            startHour = "${newHourString}:${newMinuteString}"
            showWorkingHourInfo()
        }, hour, minute, true).show()
    }

    fun showEndHourPickerDialog(view: View) {
        val hour = endHour.substringBefore(":").toInt()
        val minute = endHour.substringAfter(":").toInt()

        TimePickerDialog(this, { _, newHour, newMinute ->
            val newHourString = if (newHour < 10)  "0${newHour}" else "$newHour"
            val newMinuteString = if (newMinute < 10)  "0${newMinute}" else "$newMinute"

            endHour = "${newHourString}:${newMinuteString}"
            showWorkingHourInfo()
        }, hour, minute, true).show()
    }

    fun save(view: View) {
        val uid = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("/consultants/$uid/worktime").push()
        val workDay = WorkDays(weekDays[dayPosition], startHour, endHour)
        reference.setValue(workDay)
        finish()
    }

    companion object {
        val weekDays = arrayOf(
            "Poniedziałek",
            "Wtorek",
            "Środa",
            "Czwartek",
            "Piątek",
            "Sobota",
            "Niedziela"
        )
    }
}
