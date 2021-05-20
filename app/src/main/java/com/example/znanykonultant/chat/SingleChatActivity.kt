package com.example.znanykonultant.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.znanykonultant.R

class SingleChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_chat)

        if (intent != null) {
            val chatId = intent.getStringExtra("message")
            findViewById<TextView>(R.id.chatId).text = chatId
        }
    }
}
