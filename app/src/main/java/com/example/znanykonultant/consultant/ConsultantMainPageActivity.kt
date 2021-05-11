package com.example.znanykonultant.consultant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.znanykonultant.R

class ConsultantMainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultant_main_page)

        createRecyclerViews()
    }

    private fun createRecyclerViews() {
        val conversationsRecycler = findViewById<RecyclerView>(R.id.conversationsRecycler)
        conversationsRecycler.layoutManager = LinearLayoutManager(this)
        conversationsRecycler.adapter = ConsultantConversationListAdapter(genTempData())

        val appointmentsRecycler = findViewById<RecyclerView>(R.id.appointmentsRecycler)
        appointmentsRecycler.layoutManager = LinearLayoutManager(this)
        appointmentsRecycler.adapter = ConsultantAppointmentsListAdapter(genTempData())
    }

    private fun genTempData(): List<String> {
        return listOf("abc", "def", "xyz")
    }
}
