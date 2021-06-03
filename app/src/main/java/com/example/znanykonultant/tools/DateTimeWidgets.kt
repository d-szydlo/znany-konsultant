package com.example.znanykonultant.tools

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.InputType
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

class DateTimeWidgets(private val context: Context, private val editField : EditText) {

    private fun showDate() {
        val calendar: Calendar = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
                editField.setText(simpleDateFormat.format(calendar.time))

            }
        DatePickerDialog(
            context,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTime() {
        val calendar: Calendar = Calendar.getInstance()
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val simpleDateFormat = SimpleDateFormat("HH:mm")
                editField.setText(simpleDateFormat.format(calendar.time))
            }
        TimePickerDialog(
            context,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    fun initDate() {
        editField.inputType = InputType.TYPE_NULL
        editField.setOnClickListener {
            showDate()
        }
    }

    fun initTime() {
        editField.inputType = InputType.TYPE_NULL
        editField.setOnClickListener {
            showTime()
        }
    }
}