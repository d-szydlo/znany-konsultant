package com.example.znanykonultant.tools

class DayOfWeekConverter(private val dayOfWeek : Int) {

    private val dayMap : Map<Int, String> = mapOf(
        1 to "sunday",
        2 to "monday",
        3 to "tuesday",
        4 to "wednesday",
        5 to "thursday",
        6 to "friday",
        7 to "saturday"
    )

    fun convert(): String? {
        return dayMap[dayOfWeek]
    }
}