package com.example.znanykonultant.appointments

import com.example.znanykonultant.entity.Appointments
import com.example.znanykonultant.entity.WorkDays
import com.example.znanykonultant.tools.DateTimeConverter

class CommonFunctions {

     fun convertWorkHours(pickedDay : MutableList<WorkDays>) : String {
        var output = ""
        for( value in pickedDay) {
            output +=  "${value.start} - ${value.stop} \n"
        }
        return output
    }

     fun printTermsHours(date : String, terms : HashMap<String, MutableList<WorkDays>>) : String {
        var output = ""
        for (value in terms[date]!!) {
            output += "${value.start} - ${value.stop}\n"
        }
        return output
    }

    fun checkIfPossible(timeStart : String,
                        timeStop : String,
                        date : String,
                        terms : HashMap<String, MutableList<WorkDays>>,
                        pickedDay: MutableList<WorkDays>
    ) : Boolean {
        if(terms.containsKey(date)) {
            for (value in terms[date]!!) {
                /* IF:
                    1 -> timestart, timestop in <value.start, value.stop>
                    2 -> timestop in <value.start, value.stop> & timestart < value.start
                    3 -> timestart in <value.start, value.stop> & timestop > value.stop
                 */
                if ((value.start <= timeStart && timeStop <= value.stop) ||
                    (value.start > timeStart && timeStop >= value.start) ||
                    (timeStart < value.stop && timeStop > value.stop)
                ) { return false }

            }
        }
        for (value in pickedDay) {
            if (value.start <= timeStart && timeStop <= value.stop)
                return true
        }
        return false
    }

    fun calculateTerms(appointments : MutableList<Appointments> ) : HashMap<String, MutableList<WorkDays>> {
        val output : HashMap<String, MutableList<WorkDays>> = hashMapOf()
        for (app in appointments) {
            val dateStart = DateTimeConverter(app.timestampStart).splitConverted()
            val dateStop = DateTimeConverter(app.timestampStop).splitConverted()

            if(output.containsKey(dateStart[0]))
                output[dateStart[0]]?.add(WorkDays("",dateStart[1], dateStop[1]))
            else
                output[dateStart[0]] = mutableListOf(WorkDays("", dateStart[1], dateStop[1]))
        }

        return output

    }
}