package de.rohnert.smarteatingsystem.frontend.foodtracker.helper

import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.charts.PieChart
import java.text.DecimalFormat


class MyPercentFormatter(var pieChart:PieChart):ValueFormatter()
{
    var mFormat: DecimalFormat
    private var percentSignSeparated: Boolean = false

    init {
        mFormat = DecimalFormat("###,###,##0.0")
        percentSignSeparated = true
    }


    override fun getFormattedValue(value: Float): String {
        return mFormat.format(value) + if (percentSignSeparated) " %" else "%"
    }

    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
        return if (pieChart != null && pieChart.isUsePercentValuesEnabled) {
            // Converted to percent
            getFormattedValue(value)
        } else {
            // raw value, skip percent sign
            mFormat.format(value)
        }
    }
}