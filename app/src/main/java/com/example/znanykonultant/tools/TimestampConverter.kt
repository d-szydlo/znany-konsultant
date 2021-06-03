package com.example.znanykonultant.tools

import android.icu.text.SimpleDateFormat

class TimestampConverter(private val date: String, private val pattern : String) {

    fun convert() : Long {
        val date = SimpleDateFormat(pattern).parse(date)
        return date.time
    }

}