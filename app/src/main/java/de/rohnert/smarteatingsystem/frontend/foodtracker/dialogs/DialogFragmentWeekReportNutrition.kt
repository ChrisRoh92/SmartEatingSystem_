package de.rohnert.smarteatingsystem.frontend.foodtracker.dialogs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.frontend.foodtracker.FoodViewModel2
import de.rohnert.smarteatingsystem.frontend.foodtracker.helper.MyPercentFormatter
import de.rohnert.smarteatingsystem.frontend.foodtracker.helper.MyValueFormatter
import de.rohnert.smarteatingsystem.frontend.foodtracker.helper.WeekReportCreator

class DialogFragmentWeekReportNutrition(
    var foodViewModel: FoodViewModel2,
    var weekReport: WeekReportCreator
): DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var toolbar:Toolbar

    // Charts:
    private lateinit var chartKcal:LineChart
    private lateinit var pieChartNutrition:PieChart
    private lateinit var barChartNutrtition:BarChart

    // Chart Content:
    private var content:ArrayList<ArrayList<Float>> = ArrayList()
    private var nutritionValues:ArrayList<Float> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialog
        )


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        rootView = inflater.inflate(R.layout.dialog_weekreport_charts, container, false)

        // Toolbar initialisieren:
        initToolBar()
        initChartContent()
        initBarChart()
        initPieChart()
        initLineChart()
        // Sobald Daten bereit sind, sollen diese anschließend abgerufen werden:
        // Content abholen:




        return rootView
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Init Views
    private fun initToolBar() {
        toolbar = rootView.findViewById(R.id.dialog_weekreport_nutrition_toolbar)
        //toolbar.collapseIcon = ContextCompat.getDrawable(rootView.context,)
        toolbar.setNavigationOnClickListener {
            // handle back button naviagtion
            dismiss()
        }



    }

    // Diese Methode wird gebraucht, damit bevor die richtigen Daten bereitgestellt werden können
    // die Charts bereits angezeigt werden können:
    private fun initChartContent()
    {
        content = weekReport.content
        nutritionValues = weekReport.getNutritionValues()
    }

    private fun initLineChart()
    {
        chartKcal = rootView.findViewById(R.id.dialog_weekreport_nutrition_line_values)

        // Values hinzufügen:
        var dataSetList:ArrayList<LineDataSet> = ArrayList()
        for((index,i) in content.withIndex())
        {
            if(index != 0)
            {
                var entries:ArrayList<Entry> = ArrayList()
                for((indexJ,j) in i.withIndex())
                {
                    entries.add(Entry(indexJ.toFloat(),j))

                }
                dataSetList.add(LineDataSet(entries,""))
            }
            else
            {

            }

        }

        var colors = arrayListOf(
            ContextCompat.getColor(rootView.context,R.color.colorPrimary),
            ContextCompat.getColor(rootView.context,R.color.bar_color_1),
            ContextCompat.getColor(rootView.context,R.color.bar_color_2))
        var labels = arrayListOf("Kohlenhydrate","Protein","Fett")
        // Settings für die Entries
        for((index,i) in dataSetList.withIndex())
        {
            i.label = labels[index]
            i.color = colors[index]
            i.valueFormatter = MyValueFormatter()
            i.setDrawFilled(false)
            i.fillColor = colors[index]
            i.fillAlpha = 50
            i.mode = LineDataSet.Mode.CUBIC_BEZIER
            i.setDrawCircles(true)
            i.circleRadius = 3f
            i.circleHoleRadius = 0f
            i.setCircleColor(colors[index])
            i.lineWidth = 3f
            i.axisDependency = YAxis.AxisDependency.LEFT


        }

        // LineData
        var dataSets:ArrayList<ILineDataSet> = ArrayList()
        for(i in dataSetList) dataSets.add(i)
        /*dataSets.add(dataSet)
        dataSets.add(dataSet2)*/

        var data = LineData(dataSets)
        data.setValueFormatter(MyValueFormatter())
        data.setDrawValues(false)


        chartKcal.data = data
        //chart.legend.isEnabled = true
        chartKcal.description.isEnabled = false

        var legend = chartKcal.legend
        legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER

        var xAxis = chartKcal.xAxis
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(true)
        xAxis.setDrawLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MyValueFormatter()

        var yAxisLeft = chartKcal.axisLeft
        var yAxisRight = chartKcal.axisRight
        yAxisLeft.setDrawLabels(true)
        yAxisLeft.setDrawGridLines(true)
        yAxisRight.setDrawGridLines(false)
        yAxisLeft.isEnabled = true
        yAxisRight.isEnabled = false

        yAxisLeft.axisMinimum = 0f


        chartKcal.animateY(1000,Easing.EaseInOutCubic)


        chartKcal.extraBottomOffset = 10f
        chartKcal.invalidate()

        chartKcal.setTouchEnabled(false)
        chartKcal.isDragEnabled = false
        chartKcal.setScaleEnabled(false)
        chartKcal.setPinchZoom(false)
        chartKcal.isDoubleTapToZoomEnabled = false
        chartKcal.isDragDecelerationEnabled = false



    }

    private fun initPieChart()
    {

        pieChartNutrition = rootView.findViewById(R.id.dialog_weekreport_nutrition_piechart)

        // Content:
        var values:ArrayList<PieEntry> = ArrayList()
        values.add(PieEntry(nutritionValues[1]*4.1f,"Kohlenhydrate"))
        values.add(PieEntry(nutritionValues[2]*4.1f,"Protein"))
        values.add(PieEntry(nutritionValues[3]*9.2f,"Fett"))

        // Einstellungen:
        pieChartNutrition.setUsePercentValues(true)
        pieChartNutrition.description.isEnabled = false
        pieChartNutrition.setExtraOffsets(0f,0f,0f,10f)
        pieChartNutrition.setDrawCenterText(true)
        pieChartNutrition.setHoleColor(Color.WHITE)
        pieChartNutrition.transparentCircleRadius = 61f
        pieChartNutrition.setTransparentCircleAlpha(0)
        pieChartNutrition.dragDecelerationFrictionCoef = 0.2f
        pieChartNutrition.setDrawEntryLabels(false)




        // Legende
        var legend = pieChartNutrition.legend
        legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(true)



        // PieDataSet
        var dataSet = PieDataSet(values,"Nährwertvertteilung")
        dataSet.sliceSpace = 3f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 11f
        dataSet.label = ""
        dataSet.valueFormatter = MyPercentFormatter(pieChartNutrition)
        var colors = arrayListOf(
            ContextCompat.getColor(rootView.context,R.color.colorPrimary),
            ContextCompat.getColor(rootView.context,R.color.bar_color_1),
            ContextCompat.getColor(rootView.context,R.color.bar_color_2)
            )
        dataSet.colors = colors
        dataSet.setDrawValues(true)


        var data = PieData()
        data.dataSet = dataSet

        pieChartNutrition.data = data

        pieChartNutrition.animateY(1000,Easing.EaseInOutCubic)

        pieChartNutrition.invalidate()

        // Alle Listener deaktivieren:
        pieChartNutrition.setTouchEnabled(false)
        pieChartNutrition.isDragDecelerationEnabled = false




        // Datas

    }

    private fun initBarChart()
    {
        barChartNutrtition = rootView.findViewById(R.id.dialog_weekreport_nutrition_bar_kcal)

        // BarChartDatas erstellen:
        var entries:ArrayList<BarEntry> = ArrayList()
        for((index,i) in content[0].withIndex())
        {
            entries.add(BarEntry((index).toFloat(),i))
        }

        // Einstellungen:
        barChartNutrtition.description.isEnabled = false
        // Legende:
        var legend = barChartNutrtition.legend
        legend.isEnabled = false
        legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        // X-Achse
        var xAxis = barChartNutrtition.xAxis
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MyValueFormatter()

        // Y-Achsen
        var yAxisLeft = barChartNutrtition.axisLeft
        var yAxisRight = barChartNutrtition.axisRight
        yAxisLeft.setDrawLabels(true)
        yAxisLeft.setDrawGridLines(false)
        yAxisRight.setDrawGridLines(false)
        yAxisLeft.isEnabled = true
        yAxisRight.isEnabled = false
        yAxisLeft.axisMinimum = 0f
        yAxisRight.axisMinimum = 0f



        // BarDataSet:
        var barDataSet = BarDataSet(entries,"Energie [kcal]")
        // Set Color:
        barDataSet.color = ContextCompat.getColor(rootView.context,R.color.bar_color_1)
        barDataSet.valueTextSize = 11f
        var barData = BarData()
        barData.addDataSet(barDataSet)
        barChartNutrtition.data = barData
        barChartNutrtition.extraBottomOffset = 10f

        barChartNutrtition.animateY(1000,Easing.EaseInOutCubic)
        barChartNutrtition.invalidate()

        barChartNutrtition.setTouchEnabled(false)
        barChartNutrtition.isDragEnabled = false
        barChartNutrtition.setScaleEnabled(false)
        barChartNutrtition.setPinchZoom(false)
        barChartNutrtition.isDoubleTapToZoomEnabled = false
        barChartNutrtition.isDragDecelerationEnabled = false



        // dialog_weekreport_nutrition_bar_kcal
    }


}