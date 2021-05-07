package com.example.znanykonultant.db

import java.sql.Timestamp
import java.util.*

class Appointments(val personLogin : String, val consultantLogin : String, val timestamp: Timestamp, place : String,
                    rate : Int) {
}