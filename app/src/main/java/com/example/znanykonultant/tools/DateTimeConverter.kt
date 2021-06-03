package com.example.znanykonultant.tools

import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class DateTimeConverter(private val timestamp: Long) {

    private fun prepare(): Calendar {
        val date = 1000 * (timestamp / 1000)
        val c: Calendar = Calendar.getInstance()
        c.time = Date(date)
        c.set(Calendar.MILLISECOND, 0)

        return c
    }

    fun convert(): String {
        val c = prepare()
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
        sdf.timeZone = TimeZone.getTimeZone("GMT+2")
        return sdf.format(c.time)
    }

    fun splitConverted(): List<String> {
        return convert().split(" ")

    }

}