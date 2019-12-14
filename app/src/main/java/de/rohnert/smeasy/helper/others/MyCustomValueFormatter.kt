package de.rohnert.smeasy.helper.others

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class MyCustomValueFormatter(var values:ArrayList<String>): ValueFormatter()
{

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return values.getOrNull(value.toInt()) ?: value.toString()
    }
}