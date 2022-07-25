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
    var m_id: Int,
    var m_view: View
) {

    private lateinit var m_chart: LineChart
    private lateinit var m_dataSet: ArrayList<ILineDataSet>
    private lateinit var m_data: LineData
    private lateinit var m_legend: Legend
    private lateinit var m_xAxis: XAxis
    private lateinit var m_yAxisLeft: YAxis
    private lateinit var m_yAxisRight: YAxis
    private var m_wasAlreadyInitialized: Boolean = false
    private var m_colors = arrayListOf(
        ContextCompat.getColor(m_view.context, R.color.bar_color_1),
        ContextCompat.getColor(m_view.context, R.color.bar_color_2),
        ContextCompat.getColor(m_view.context, R.color.bar_color_3),
        ContextCompat.getColor(m_view.context, R.color.bar_color_4),
        ContextCompat.getColor(m_view.context, R.color.bar_color_5),
        ContextCompat.getColor(m_view.context, R.color.bar_color_6)
    )

    /**
     * @brief: Central API to update the LineChart with new Data
     * @param values: List of Entries to be rendered
     * @param xValues: List of the to render xValues (Monday, Thusday...)
     */
    fun updateChart(values: ArrayList<ArrayList<Entry>>, xValues: ArrayList<String>) {
        // Chart initialisieren:
        m_chart = m_view.findViewById(m_id)
        m_dataSet = ArrayList()
        for ((index, i) in values.withIndex()) {
            m_dataSet.add(getDataSetFromList(i, index))
        }

        setChartData(m_dataSet, xValues)

        if (true) {
            setChartLegend()
            setXAxis(xValues)
            setYAxis()
            disableTouchListener()
            m_wasAlreadyInitialized = true
            m_chart.extraBottomOffset = 10.0F
            m_chart.extraLeftOffset = 10.0F
        }

        m_chart.invalidate()
    }

    /**
     * @brief: Fills the Chart with the dataset
     * @param dataSet: List of ILineData to be put in the chart
     * @param xValues: List of the to render xValues (Monday, Thusday...)
     */
    private fun setChartData(dataSet: ArrayList<ILineDataSet>, xValues: ArrayList<String>) {
        // Data initialisieren:
        m_data = LineData(dataSet)
        m_data.setValueFormatter(MyCustomValueFormatter(xValues))
        m_data.setDrawValues(false)
        m_chart.data = m_data
        m_chart.description.isEnabled = false
    }

    /**
     * @brief: Default Settings for the Chart Legend
     */
    private fun setChartLegend() {
        m_legend = m_chart.legend
        m_legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
        m_legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        m_legend.setDrawInside(false)
        m_legend.isWordWrapEnabled = true
        m_legend.isEnabled = false
    }

    /**
     * @brief: Default Settings for X Axis of the LineChart
     * @param xValues: List of the to render xValues (Monday, Thusday...)
     */
    private fun setXAxis(xValues: ArrayList<String>) {
        m_xAxis = m_chart.xAxis
        m_xAxis.setDrawAxisLine(true)
        m_xAxis.setDrawGridLines(false)
        m_xAxis.setDrawLabels(true)
        m_xAxis.position = XAxis.XAxisPosition.BOTTOM
        m_xAxis.granularity = 1f
        m_xAxis.valueFormatter = MyCustomValueFormatter(xValues)
        m_xAxis.labelCount = 5
        m_xAxis.spaceMin = 0.5F
        m_xAxis.spaceMax = 0.5F
    }

    /**
     * @brief: Default Settings for Y Axis of the LineChart
     */
    private fun setYAxis() {
        m_yAxisLeft = m_chart.axisLeft
        m_yAxisRight = m_chart.axisRight
        m_yAxisLeft.setDrawLabels(true)
        m_yAxisLeft.setDrawGridLines(false)
        m_yAxisRight.setDrawGridLines(false)
        m_yAxisLeft.isEnabled = true
        m_yAxisRight.isEnabled = false
    }

    /**
     * @brief: Disable the Touch Listener for the Line Chart
     */
    private fun disableTouchListener() {
        m_chart.setTouchEnabled(false)
        m_chart.isDragEnabled = false
        m_chart.setScaleEnabled(false)
        m_chart.setPinchZoom(false)
        m_chart.isDoubleTapToZoomEnabled = false
        m_chart.isDragDecelerationEnabled = false
    }

    /**
     * @brief: Gets of Entry Object a LineDataSet
     * @param values: List of Entries
     * @param pos: Position of Values in List
     * @param dotted: Mark if Line should be dotted renderd
     */
    private fun getDataSetFromList(
        values: ArrayList<Entry>, pos: Int, dotted: Boolean = false
    ): LineDataSet {
        var dataSet = LineDataSet(values, "")
        dataSet.color = m_colors[pos]
        dataSet.setDrawFilled(false)
        dataSet.fillColor = m_colors[pos]
        dataSet.fillAlpha = 50
        dataSet.mode = LineDataSet.Mode.LINEAR
        if (dotted) dataSet.enableDashedLine(10f, 5f, 0f)
        dataSet.setDrawCircles(true)
        dataSet.circleRadius = 5f
        dataSet.circleHoleRadius = 0f
        dataSet.setCircleColor(m_colors[pos])
        dataSet.setDrawCircleHole(false)
        dataSet.lineWidth = 2f

        return dataSet

    }
}