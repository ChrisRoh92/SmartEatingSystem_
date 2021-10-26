package de.rohnert.smarteatingsystem.utils.others

import android.util.Log
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import de.rohnert.smarteatingsystem.utils.Constants.LOGGING_TAG_ANALYSIS

class MyCustomValueFormatter(var values:ArrayList<String>): ValueFormatter()
{

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//        Log.d(LOGGING_TAG_ANALYSIS,"Label: ${values[value.toInt()]}")
        return values.getOrNull(value.toInt()) ?: value.toString()
    }
}