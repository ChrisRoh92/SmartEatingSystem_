package de.rohnert.smeasy.helper.standard_charts

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.foodtracker.helper.MyValueFormatter
import de.rohnert.smeasy.helper.others.MyCustomValueFormatter

class StandardLineChart(var id:Int, var view: View, var content:ArrayList<ArrayList<Entry>>,var labels:ArrayList<String>)
{
    private lateinit var chart: LineChart
    private lateinit var dataSet: ArrayList<ILineDataSet>
    private lateinit var data: LineData
    private lateinit var legend: Legend
    private lateinit var xAxis:XAxis
    private lateinit var yAxisLeft:YAxis
    private lateinit var yAxisRight:YAxis

    // Farben:
    private  var colors = arrayListOf(
        ContextCompat.getColor(view.context, R.color.bar_color_1),
        ContextCompat.getColor(view.context, R.color.bar_color_2),
        ContextCompat.getColor(view.context, R.color.bar_color_3),
        ContextCompat.getColor(view.context, R.color.bar_color_4),
        ContextCompat.getColor(view.context, R.color.bar_color_5),
        ContextCompat.getColor(view.context, R.color.bar_color_6)
    )

    init {
        initPieChart()
    }

    private fun initPieChart()
    {
        // Chart initialisieren:
        chart = view.findViewById(id)
        dataSet = ArrayList()
        for((index,i) in content.withIndex())
        {
            dataSet.add(getDataSetFromList(i,index,(index%2 != 0)))
        }



        fun setChartData()
        {
            // Data initialisieren:
            data = LineData(dataSet)
            data.setValueFormatter(MyCustomValueFormatter(arrayListOf("Mo","Di","Mi","Do","Fr","Sa","So")))
            data.setDrawValues(false)
            chart.data = data
            chart.description.isEnabled = false
        }

        fun setChartLegend()
        {
            legend = chart.legend
            legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.setDrawInside(false)
            legend.isWordWrapEnabled = true

        }

        fun setXAxis()
        {
            xAxis = chart.xAxis
            xAxis.setDrawAxisLine(true)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = MyValueFormatter()
        }

        fun setYAxis()
        {
            yAxisLeft = chart.axisLeft
            yAxisRight = chart.axisRight
            yAxisLeft.setDrawLabels(true)
            yAxisLeft.setDrawGridLines(false)
            yAxisRight.setDrawGridLines(false)
            yAxisLeft.isEnabled = true
            yAxisRight.isEnabled = false

            yAxisLeft.axisMinimum = 0f
            yAxisRight.axisMinimum = 0f
        }

        setChartData()
        setChartLegend()
        setXAxis()
        setYAxis()

        // Diagramm starten:
        chart.extraBottomOffset = 10f
        chart.invalidate()

        fun disableTouchListener()
        {
            chart.setTouchEnabled(false)
            chart.isDragEnabled = false
            chart.setScaleEnabled(false)
            chart.setPinchZoom(false)
            chart.isDoubleTapToZoomEnabled = false
            chart.isDragDecelerationEnabled = false

        }

        disableTouchListener()






    }

    private fun getDataSetFromList(values:ArrayList<Entry>, pos:Int,dotted:Boolean = false):LineDataSet
    {
        var dataSet= LineDataSet(values,labels[pos])
        dataSet.label = labels[pos]
        dataSet.color = colors[pos]
        dataSet.setDrawFilled(false)
        dataSet.fillColor = colors[pos]
        dataSet.fillAlpha = 50
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        if(dotted) dataSet.enableDashedLine(10f,5f,0f)
        dataSet.setDrawCircles(false)
        dataSet.circleRadius = 2f
        dataSet.circleHoleRadius = 0f
        dataSet.setCircleColor(colors[pos])
        dataSet.setDrawCircleHole(false)
        dataSet.lineWidth = 3f

        return dataSet

    }

    fun updateLineChart(content:ArrayList<ArrayList<Entry>>)
    {
        this.content = content
        dataSet = ArrayList()
        for((index,i) in content.withIndex())
        {
            dataSet.add(getDataSetFromList(i,index,false))
        }
        Log.d("Smeasy","StandardLineChart - updateLineChart dateSet: $dataSet")
        data = LineData(dataSet)

        data.setValueFormatter(MyCustomValueFormatter(arrayListOf("Mo","Di","Mi","Do","Fr","Sa","So")))
        data.setDrawValues(false)
        chart.data = data
        chart.description.isEnabled = false

        chart.notifyDataSetChanged()
        chart.invalidate()

    }
}