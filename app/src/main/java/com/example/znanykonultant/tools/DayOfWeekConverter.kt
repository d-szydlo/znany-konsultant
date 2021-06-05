package com.example.znanykonultant.tools

class DayOfWeekConverter(private val dayOfWeek : Int) {

    private val dayMap : Map<Int, String> = mapOf(
        1 to "Niedziela",
        2 to "Poniedziałek",
        3 to "Wtorek",
        4 to "Środa",
        5 to "Czwartek",
        6 to "Piątek",
        7 to "Sobota"

    )

    fun convert(): String? {
        return dayMap[dayOfWeek]
    }


}