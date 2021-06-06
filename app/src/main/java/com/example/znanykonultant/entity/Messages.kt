package com.example.znanykonultant.entity

class Messages(val id: String, val text: String, val fromId: String, val toId: String, var timestamp: Long) {
    constructor() : this("", "", "", "", -1)
}
