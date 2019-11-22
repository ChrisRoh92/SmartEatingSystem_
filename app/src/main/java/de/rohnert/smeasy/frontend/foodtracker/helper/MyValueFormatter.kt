package de.rohnert.smeasy.frontend.foodtracker.helper

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class MyValueFormatter:ValueFormatter()
{

    private val days = arrayOf("Mo", "Di", "Mi", "Do", "Fr", "Sa", "So")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return days.getOrNull(value.toInt()) ?: value.toString()
    }
}