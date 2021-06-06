package com.example.znanykonultant.consultant.profile

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.Consultant
import com.example.znanykonultant.entity.PageVisit
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class ConsultantVisitorsChartFragment : Fragment() {

    private lateinit var chart : BarChart
    private lateinit var database : DatabaseReference
    private val dayInMillis : Long = 86400000
    private var now : Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_visitors_chart, container, false)
        chart = view.findViewById(R.id.chart)
        now = System.currentTimeMillis()
        loadData()
        return view
    }

    private fun loadData() {
        var data = mutableListOf<PageVisit>()
        database = Firebase.database.reference
        database.child("pagevisit").get().addOnSuccessListener { dataSnapshot ->
            dataSnapshot.children.mapNotNullTo(data) {
                it.getValue(PageVisit::class.java)
            }
            val consultantUid = FirebaseAuth.getInstance().currentUser!!.uid
            data = data.filter { it.consultant == consultantUid } as MutableList<PageVisit>
            loadChart(data)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun loadChart(data : MutableList<PageVisit>){
        val entries = prepareData(data)
        val barDataSet = BarDataSet(entries, "Liczba wyświetleń profilu każdego dnia w ciągu ostatniego tygodnia")
        val barData = BarData(barDataSet)
        chart.data = barData
        val description = Description()
        description.text = ""
        chart.description = description
        chart.setGridBackgroundColor(Color.WHITE)
        chart.invalidate()
    }

    private fun prepareData(data : MutableList<PageVisit>) : MutableList<BarEntry> {
        val entries : MutableList<BarEntry> = mutableListOf()

        for (i in 1..7){
            val entryData = data.filter {
                it.timestamp > now - i*dayInMillis && it.timestamp < now - (i-1)*dayInMillis
            } as MutableList<PageVisit>
            entries.add(BarEntry(i.toFloat(), entryData.size.toFloat()))
        }

        return entries
    }

}