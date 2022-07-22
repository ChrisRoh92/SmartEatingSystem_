package de.rohnert.smarteatingsystem.utils.standard_charts

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.ui.foodtracker.helper.MyValueFormatter
import de.rohnert.smarteatingsystem.utils.Constants
import de.rohnert.smarteatingsystem.utils.others.MyCustomValueFormatter

class StandardLineChart(
    var id: Int,
    var view: View,
    var yContent: ArrayList<Double>,
    var xValues: ArrayList<String>,
    var labels: ArrayList<String> = arrayListOf("Set 1", "Set 2"),
    var yIndices: ArrayList<Float> = ArrayList()
) {
    private lateinit var chart: LineChart
    private lateinit var dataSet: ArrayList<ILineDataSet>
    private lateinit var data: LineData
    private lateinit var legend: Legend
    private lateinit var xAxis: XAxis
    private lateinit var yAxisLeft: YAxis
    private lateinit var yAxisRight: YAxis

    private var chartData: ArrayList<ArrayList<Entry>> = ArrayList()

    // Farben:
    private var colors = arrayListOf(
        ContextCompat.getColor(view.context, R.color.bar_color_1),
        ContextCompat.getColor(view.context, R.color.bar_color_2),
        ContextCompat.getColor(view.context, R.color.bar_color_3),
        ContextCompat.getColor(view.context, R.color.bar_color_4),
        ContextCompat.getColor(view.context, R.color.bar_color_5),
        ContextCompat.getColor(view.context, R.color.bar_color_6)
    )

    init {
        transformInputData()
        initLineChart()


    }

    private fun transformInputData() {
        chartData = ArrayList()
        chartData.add(ArrayList())
        if (yIndices.isNotEmpty() && yIndices.size == yContent.size) {
            for (i in yContent.indices) {
                chartData[0].add(Entry(yIndices[i], yContent[i].toFloat()))
            }
        } else {
            for ((index, i) in yContent.withIndex()) {
                chartData[0].add(Entry(index.toFloat(), i.toFloat()))
            }
        }

    }

    private fun setChartData() {
        // Data initialisieren:
        data = LineData(dataSet)


        data.setValueFormatter(MyCustomValueFormatter(xValues))
        data.setDrawValues(false)
        chart.data = data
        chart.description.isEnabled = false
    }

    private fun initLineChart() {
        // Chart initialisieren:
        chart = view.findViewById(id)
        dataSet = ArrayList()
        for ((index, i) in chartData.withIndex()) {
            dataSet.add(getDataSetFromList(i, index))
        }

        fun setChartLegend() {
            legend = chart.legend
            legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.setDrawInside(false)
            legend.isWordWrapEnabled = true
            legend.isEnabled = false

        }

        fun setXAxis() {
            xAxis = chart.xAxis
            xAxis.setDrawAxisLine(true)
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.valueFormatter = MyCustomValueFormatter(xValues)
            xAxis.labelCount = 5
            xAxis.spaceMin = 0.5F
            xAxis.spaceMax = 0.5F

        }

        fun setYAxis() {
            yAxisLeft = chart.axisLeft
            yAxisRight = chart.axisRight
            yAxisLeft.setDrawLabels(true)
            yAxisLeft.setDrawGridLines(false)
            yAxisRight.setDrawGridLines(false)
            yAxisLeft.isEnabled = true
            yAxisRight.isEnabled = false

            var yMin = (yContent.min() ?: 0.0).toFloat()
            var yMax = (yContent.max() ?: 0.0).toFloat()
            if (yMin > 0) {
                yMin -= 10
            }
            if (yMax > 0) {
                yMax += 10
            }

            yAxisLeft.axisMinimum = yMin
            yAxisLeft.axisMaximum = yMax
        }

        fun disableTouchListener() {
            chart.setTouchEnabled(false)
            chart.isDragEnabled = false
            chart.setScaleEnabled(false)
            chart.setPinchZoom(false)
            chart.isDoubleTapToZoomEnabled = false
            chart.isDragDecelerationEnabled = false

        }

        setChartData()
        setChartLegend()
        setXAxis()
        setYAxis()
        disableTouchListener()

        // Diagramm starten:
        chart.extraBottomOffset = 10f
        chart.extraLeftOffset = 10.0F

//        chart.setVisibleXRange(7F, xValues.size.toFloat())
        chart.invalidate()
    }


    private fun updateChart() {

        // Aktuellen Chart leeren
        chart.clear()
        // Neue Daten Laden:
        transformInputData()
        dataSet = ArrayList()
        for ((index, i) in chartData.withIndex()) {
            dataSet.add(getDataSetFromList(i, index, false))
        }
        // Neue Daten Laden
        setChartData()
        // Set new Axis Limits
        updateAxisLimits()

        chart.notifyDataSetChanged()
        chart.invalidate()
    }

    private fun getDataSetFromList(
        values: ArrayList<Entry>,
        pos: Int,
        dotted: Boolean = false
    ): LineDataSet {
        var dataSet = LineDataSet(values, "")
        dataSet.label = labels[pos]
        dataSet.color = colors[pos]
        dataSet.setDrawFilled(false)
        dataSet.fillColor = colors[pos]
        dataSet.fillAlpha = 50
        dataSet.mode = LineDataSet.Mode.LINEAR
        if (dotted) dataSet.enableDashedLine(10f, 5f, 0f)
        dataSet.setDrawCircles(true)
        dataSet.circleRadius = 5f
        dataSet.circleHoleRadius = 0f
        dataSet.setCircleColor(colors[pos])
        dataSet.setDrawCircleHole(false)
        dataSet.lineWidth = 2f

        return dataSet

    }

    fun updateLineChart(yContent: ArrayList<Double>, xContent: ArrayList<String>) {
        this.yContent = yContent
        this.xValues = xContent

        updateChart()


/*        data = LineData(dataSet)

        data.setValueFormatter(MyCustomValueFormatter(xContent))
        data.setDrawValues(false)
        chart.data = data
        chart.description.isEnabled = false
        xAxis.valueFormatter = MyCustomValueFormatter(labels)
        updateAxisLimits()

        chart.notifyDataSetChanged()
        chart.invalidate()*/

    }

    private fun updateAxisLimits(min: Float = 2f, max: Float = 2f) {
        var yMin = (yContent.min() ?: 0.0).toFloat()
        var yMax = (yContent.max() ?: 0.0).toFloat()
        if (yMin > 0) {
            yMin -= min
        }
        if (yMax > 0) {
            yMax += max
        }

        yAxisLeft.axisMinimum = yMin
        yAxisLeft.axisMaximum = yMax

        xAxis.valueFormatter = MyCustomValueFormatter(xValues)

    }
}