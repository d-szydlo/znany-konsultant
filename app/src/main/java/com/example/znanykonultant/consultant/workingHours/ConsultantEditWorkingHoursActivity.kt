package com.example.znanykonultant.consultant.workingHours

import android.app.AlertDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.znanykonultant.R
import com.example.znanykonultant.databinding.ActivityConsultantEditWorkingHoursBinding
import com.example.znanykonultant.entity.WorkDays
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ConsultantEditWorkingHoursActivity : AppCompatActivity() {
    private lateinit var binding : ActivityConsultantEditWorkingHoursBinding

    private val uid = FirebaseAuth.getInstance().uid
    private val reference = FirebaseDatabase.getInstance().getReference("/consultants/$uid/worktime")

    private var workDayId : String? = null

    private var startHour = "08:00"
    private var endHour = "16:00"
    private var dayPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultantEditWorkingHoursBinding.inflate(layoutInflater)
        val view = binding.root

        checkMode()
        showWorkingHourInfo()
        setContentView(view)
    }

    private fun checkMode() {
        workDayId = intent.getStringExtra(ConsultantWorkingHoursAdapter.WORK_DAY_ID)
        if (workDayId != null)
            setEditMode(workDayId!!)
        else
            setAddTMode()
    }

    private fun setAddTMode() {
        binding.edit.visibility = View.GONE
        binding.delete.visibility = View.GONE
    }

    private fun setEditMode(workDaysId: String) {
        binding.add.visibility = View.GONE

        reference.child(workDaysId).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val workDay = snapshot.getValue(WorkDays::class.java) ?: return
                dayPosition = weekDays.indexOf(workDay.day)
                startHour = workDay.start
                endHour = workDay.stop
                showWorkingHourInfo()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
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
        val newReference = reference.push()
        val workDay = WorkDays(newReference.key!!, weekDays[dayPosition], startHour, endHour)
        newReference.setValue(workDay)
        finish()
    }

    fun edit(view: View) {
        val workDay = WorkDays(workDayId!!, weekDays[dayPosition], startHour, endHour)
        reference.child(workDayId!!).setValue(workDay)
        finish()
    }

    fun delete(view: View) {
        reference.child(workDayId!!).removeValue()
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
