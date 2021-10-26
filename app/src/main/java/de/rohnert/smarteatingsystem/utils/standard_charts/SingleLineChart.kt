package de.rohnert.smarteatingsystem.utils.standard_charts

import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import de.rohnert.smarteatingsystem.ui.foodtracker.helper.MyValueFormatter
import de.rohnert.smarteatingsystem.utils.others.MyCustomValueFormatter

class SingleLineChart(var id:Int, var rootView: View, var yValues:ArrayList<Float>, var xLabes:ArrayList<String>)
{

    private lateinit var chart:LineChart
    // Daten:
    private var entries = ArrayList<Entry>()
    private lateinit var lineDataSet:LineDataSet
    private lateinit var lineData: LineData


    init {
        initLineChart()
    }

    private fun initLineChart()
    {
        // LineChart initialisieren:
        chart = rootView.findViewById(id)

        // yValues in entries eintragen:
        entries.clear()
        for(i in yValues.indices)
        {
            entries.add(Entry(i.toFloat(),yValues[i]))
        }

        // LineDataSet erstellen:
        lineDataSet = LineDataSet(entries,"Verlauf Gewicht [kg]")


        // LineData erstellen:
        lineData = LineData(lineDataSet)

        // xAxis konfigurieren
        val xAxis = chart.xAxis
        xAxis.granularity = 1f
        xAxis.valueFormatter = MyCustomValueFormatter(arrayListOf("Montag","Dienstag","Mittwoch","Donnerstag","Freitag"))
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Daten dem Chart hinzufügen:
        chart.data = lineData
        chart.invalidate()

    }
}