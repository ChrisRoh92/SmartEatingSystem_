package de.rohnert.smarteatingsystem.ui.statistics.charts

import android.view.View
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.databases.body_database.Body
import de.rohnert.smarteatingsystem.utils.getStringFromDate
import de.rohnert.smarteatingsystem.utils.others.MyCustomValueFormatter
import java.util.*
import kotlin.collections.ArrayList

class WeightLineChart(var id:Int, var rootView: View,var bodyMap:Map<Int, Body>,var dateList:ArrayList<Date>)
{
    // Chart:
    private lateinit var chart:LineChart
    // Chart Data:
    // Daten:
    private lateinit var entries:ArrayList<Entry>
    private lateinit var lineDataSet: LineDataSet
    private lateinit var lineData: LineData
    private var maxWeight:Float = 0f
    private var minWeight:Float = 0f
    // Achsen:
    private lateinit var legend:Legend
    private lateinit var xAxis: XAxis
    private lateinit var yAxisLeft: YAxis
    private lateinit var yAxisRight: YAxis
    // Achsbeschriftung:
    private lateinit var xValues:ArrayList<String>
    private  var colors = arrayListOf(
        ContextCompat.getColor(rootView.context, R.color.bar_color_1),
        ContextCompat.getColor(rootView.context, R.color.bar_color_2),
        ContextCompat.getColor(rootView.context, R.color.bar_color_3),
        ContextCompat.getColor(rootView.context, R.color.bar_color_4),
        ContextCompat.getColor(rootView.context, R.color.bar_color_5),
        ContextCompat.getColor(rootView.context, R.color.bar_color_6)
    )



    fun init()
    {
        // Chart initialisieren:
        chart = rootView.findViewById(id)

        // Daten erstellen:
        // Liste mit Entries erstellen
        entries = ArrayList()
        for(entry in bodyMap)
        {
            entries.add(Entry(entry.key.toFloat(),entry.value.weight))
        }
        // LineDataSet erstellen
        lineDataSet = LineDataSet(entries,"Verlauf Gewicht [kg]")
        setDataStyle()
        // LineData daraus erstellen:
        lineData = LineData(lineDataSet)



        // Beschriftung erstellen:
        xValues = ArrayList()
        for(i in dateList)
            xValues.add(getStringFromDate(i,"dd/MM"))
        lineData.setValueFormatter(MyCustomValueFormatter(xValues))
        lineData.setDrawValues(false)

        // Einstellungen zum Chart erstellen:
        setChartLegend()
        setXAxis()
        setYAxis()





        // Chart Daten initialisieren:
        chart.data = lineData
        chart.description.isEnabled = false
        chart.extraBottomOffset = 10f
//        chart.setVisibleXRange(10f,xValues.size.toFloat())

        // Chart anzeigen:
        chart.invalidate()

    }


    fun updateChart(bodyMap:Map<Int, Body>,dateList:ArrayList<Date>)
    {
        this.bodyMap = bodyMap
        this.dateList = dateList

        updateChartDate()
    }

    private fun updateChartDate()
    {
        entries = ArrayList()
        for(entry in bodyMap)
        {
            entries.add(Entry(entry.key.toFloat(),entry.value.weight))
        }
        // LineDataSet erstellen
        lineDataSet = LineDataSet(entries,"Verlauf Gewicht [kg]")
        setDataStyle()
        // LineData daraus erstellen:
        lineData = LineData(lineDataSet)

        // Achsbeschriftung anpassen:
        xValues = ArrayList()
        for(i in dateList)
            xValues.add(getStringFromDate(i,"dd/MM"))
        lineData.setValueFormatter(MyCustomValueFormatter(xValues))
        lineData.setDrawValues(false)
        xAxis.valueFormatter = MyCustomValueFormatter(xValues)

        updateYAxisLimit()


        chart.data = lineData
//        chart.setVisibleXRange(10f,dateList.lastIndex.toFloat())
        chart.notifyDataSetChanged()
        chart.invalidate()


    }
    
    
    
    
    // Utils:
    private fun getMaxWeight():Float
    {

        return bodyMap.maxBy { it.value.weight }?.value!!.weight
    }
    
    private fun getMinWeight():Float
    {

        return bodyMap.minBy { it.value.weight }?.value!!.weight
    }




















    // Private Methoden:
    private fun setChartLegend()
    {
        legend = chart.legend
        legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.setDrawInside(false)
        legend.isWordWrapEnabled = true
        legend.isEnabled = false

    }

    private fun setXAxis()
    {
        xAxis = chart.xAxis
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(true)
        xAxis.setDrawLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = MyCustomValueFormatter(xValues)
        xAxis.labelCount = 10
        xAxis.labelRotationAngle = -90f

    }

    private fun setYAxis()
    {
        yAxisLeft = chart.axisLeft
        yAxisRight = chart.axisRight
        yAxisLeft.setDrawLabels(true)
        yAxisLeft.setDrawGridLines(true)
        yAxisRight.setDrawGridLines(false)
        yAxisLeft.isEnabled = true
        yAxisRight.isEnabled = false



    }

    private fun setDataStyle()
    {
        lineDataSet.label = "Verlauf Gewicht [kg]"
        lineDataSet.color = colors[0]
        lineDataSet.setDrawFilled(false)
        lineDataSet.fillColor = colors[0]
        lineDataSet.fillAlpha = 50
        lineDataSet.mode = LineDataSet.Mode.LINEAR

        lineDataSet.setDrawCircles(false)
        lineDataSet.circleRadius = 3f
        lineDataSet.circleHoleRadius = 0f
        lineDataSet.setCircleColor(colors[0])
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.lineWidth = 2f
    }

    private fun disableTouchListener()
    {
        chart.setTouchEnabled(false)
        chart.isDragEnabled = false
        chart.setScaleEnabled(false)
        chart.setPinchZoom(false)
        chart.isDoubleTapToZoomEnabled = false
        chart.isDragDecelerationEnabled = false

    }

    private fun updateYAxisLimit()
    {
        var yMax = (getMaxWeight() ?: 0f)
        var yMin = (getMinWeight() ?: 0f)

        if(yMin > 0)
        {
            yMin -= 1
        }
        if(yMax > 0)
        {
            yMax += 1
        }

        yAxisLeft.axisMinimum = yMin
        yAxisLeft.axisMaximum = yMax
    }
}