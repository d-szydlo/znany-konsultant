package com.example.znanykonultant.entity

import com.google.firebase.database.Exclude
import java.sql.Timestamp

class Messages(val id: String, val text: String, val fromId: String, val toId: String, var timestamp: Long) {
    constructor() : this("", "", "", "", -1)
}
