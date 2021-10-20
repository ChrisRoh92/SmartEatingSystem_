package de.rohnert.smarteatingsystem.ui.statistics.charts

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class DateAxisFormatter(val days:Array<String>): ValueFormatter()
{

//    private val days = arrayOf("Mo", "Di", "Mi", "Do", "Fr", "Sa", "So")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
}