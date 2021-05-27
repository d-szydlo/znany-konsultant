package com.example.znanykonultant.consultant.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.znanykonultant.R
import com.example.znanykonultant.entity.PageVisit
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.random.Random

class VisitorsChartFragment : Fragment() {

    private lateinit var chart : BarChart
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
        val data = genData()
        val entries = prepareData(data)
        val barDataSet = BarDataSet(entries, "Liczba wyświetleń w ciągu ostatniego tygodnia")
        val barData = BarData(barDataSet)
        chart.data = barData
        val description = Description()
        description.text = ""
        chart.description = description
        chart.setGridBackgroundColor(Color.WHITE)
        chart.invalidate()
    }

    private fun genData() : MutableList<PageVisit> {
        val data = mutableListOf<PageVisit>()

        for (i in 0..50){
            val timestamp = now - dayInMillis * Random.nextInt(1,8) + Random.nextLong(dayInMillis)
            val p = PageVisit("adfdvasf", "fdabdfb", timestamp)
            data.add(p)
        }

        return data
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