package de.rohnert.smeasy.helper.standard_charts

import android.view.View
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.foodtracker.helper.MyValueFormatter

class StandardBarChart(var id:Int,var view:View,var content:ArrayList<BarEntry>)
{

    private lateinit var chart: BarChart
    private lateinit var dataSet: BarDataSet
    private lateinit var data: BarData
    private lateinit var legend: Legend
    private lateinit var xAxis: XAxis
    private lateinit var yAxisLeft: YAxis
    private lateinit var yAxisRight: YAxis

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
        initChart()
    }

    private fun initChart()
    {
        // Chart initialisieren:
        chart = view.findViewById(id)

        fun setChartSettings()
        {
            chart.description.isEnabled = false
        }

        fun setChartLegend()
        {
            legend = chart.legend
            legend.isEnabled = false
            legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER

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

        fun setDataSet()
        {
            // BarDataSet:
            dataSet = BarDataSet(content,"Energie [kcal]")
            // Set Color:
            dataSet.color = colors[0]
            dataSet.valueTextSize = 11f
        }

        fun setChartData()
        {
            data = BarData()
            data.addDataSet(dataSet)
        }



        setChartSettings()
        setChartLegend()
        setXAxis()
        setYAxis()
        setDataSet()
        setChartData()

        chart.data = data
        chart.extraBottomOffset = 10f

        chart.animateY(1000, Easing.EaseInOutCubic)
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


    fun updateBarData(content:ArrayList<BarEntry>)
    {
        this.content = content
        dataSet = BarDataSet(content,"Energie [kcal]")
        dataSet.color = colors[0]
        dataSet.valueTextSize = 11f
        data.removeDataSet(0)
        data.addDataSet(dataSet)
        data.notifyDataChanged()
        chart.notifyDataSetChanged()
        chart.animateY(1000, Easing.EaseInOutCubic)
        chart.invalidate()
    }
}