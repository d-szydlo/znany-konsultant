package com.example.znanykonultant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun runMain(view: View) {
        val myintent = Intent(this, MainPageActivity::class.java)
        startActivityForResult(myintent, 1)
    }
}