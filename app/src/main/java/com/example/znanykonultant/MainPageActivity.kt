package com.example.znanykonultant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        initActivitySpinner()
    }

    private fun initActivitySpinner() {

        val picSpinner = findViewById<Spinner>(R.id.specialitySpinner)
        val types = resources.getStringArray(R.array.specialities)
        val adapter = SpinnerAdapter(this,  types)
        picSpinner.adapter = adapter
    }

}