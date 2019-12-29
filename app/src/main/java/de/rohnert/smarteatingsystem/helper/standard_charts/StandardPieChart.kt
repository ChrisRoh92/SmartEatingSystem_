package de.rohnert.smarteatingsystem.helper.standard_charts

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.frontend.foodtracker.helper.MyPercentFormatter

class StandardPieChart(var id:Int, var view: View, var content:ArrayList<PieEntry>)
{

    private lateinit var chart:PieChart
    private lateinit var dataSet:PieDataSet
    private lateinit var data:PieData

    // Farben:
    private  var colors = arrayListOf(
        ContextCompat.getColor(view.context, R.color.colorPrimary),
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
        // Stuff initialisieren:
        chart = view.findViewById(id)

        dataSet = PieDataSet(content,"")

        // Einstellungen:
        fun setChartSettings()
        {
            // Einstellungen:
            chart.setUsePercentValues(true)
            chart.description.isEnabled = false
            chart.setExtraOffsets(0f,0f,0f,10f)
            chart.setDrawCenterText(true)
            chart.setHoleColor(Color.WHITE)
            chart.transparentCircleRadius = 61f
            chart.setTransparentCircleAlpha(0)
            chart.dragDecelerationFrictionCoef = 0.2f
            chart.setDrawEntryLabels(false)
        }

        fun setChartLegend()
        {
            // Legende
            var legend = chart.legend
            legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend.setDrawInside(true)
        }

        fun setPieDataSet()
        {
            // PieDataSet
            dataSet = PieDataSet(content,"")
            dataSet.sliceSpace = 3f
            dataSet.valueTextColor = Color.WHITE
            dataSet.valueTextSize = 11f
            dataSet.label = ""
            dataSet.valueFormatter = MyPercentFormatter(chart)
            dataSet.colors = getColors()
            dataSet.setDrawValues(true)
        }



        setChartSettings()
        setChartLegend()
        setPieDataSet()

        data = PieData()
        data.dataSet = dataSet

        chart.data = data

        chart.animateY(1000, Easing.EaseInOutCubic)

        chart.invalidate()

        // Alle Listener deaktivieren:
        chart.setTouchEnabled(false)
        chart.isDragDecelerationEnabled = false

    }

    fun updatePieChart(content:ArrayList<PieEntry>)
    {
        this.content = content
        dataSet = PieDataSet(content,"")
        dataSet.sliceSpace = 3f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 11f
        dataSet.label = ""
        dataSet.valueFormatter = MyPercentFormatter(chart)
        dataSet.colors = getColors()
        dataSet.setDrawValues(true)
        data.dataSet = dataSet



        data.notifyDataChanged()
        chart.notifyDataSetChanged()
        chart.invalidate()
        // Hier noch eine Methode aufrufen, welche die Daten aktualisiert:
    }



    // Helper Stuff:
    private fun getColors():ArrayList<Int>
    {
        var export:ArrayList<Int> = ArrayList()
        for((index,i) in content.withIndex())
        {
            export.add(colors[index])
        }

        return export
    }
}