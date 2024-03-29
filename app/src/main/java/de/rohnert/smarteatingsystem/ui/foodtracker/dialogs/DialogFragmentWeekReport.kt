package de.rohnert.smarteatingsystem.ui.foodtracker.dialogs

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.data.helper.Helper
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.ui.foodtracker.viewmodel.FoodViewModel
import de.rohnert.smarteatingsystem.ui.foodtracker.adapter.WeekReportRecyclerViewAdapter
import de.rohnert.smarteatingsystem.ui.foodtracker.helper.MyValueFormatter
import de.rohnert.smarteatingsystem.ui.foodtracker.helper.WeekReportCreator
import de.rohnert.smarteatingsystem.utils.dialogs.DialogLoading
import java.util.*
import kotlin.collections.ArrayList

class DialogFragmentWeekReport(var foodViewModel: FoodViewModel): DialogFragment(),
    View.OnClickListener {


    // Allgemeine Variablen:
    private lateinit var rootView: View
    private var helper = Helper()
    private var mDate:String = foodViewModel.date
    private lateinit var reporter:WeekReportCreator

    // Loading Dialog
    private lateinit var dialogLoader:DialogLoading

    // Chart:
    private lateinit var chart: LineChart

    // View Element:
    private lateinit var toolbar:Toolbar
    // Buttons
    private lateinit var btnNutrition:Button

    // TestView:
    private var titleTextList:ArrayList<TextView> = ArrayList()
    private var subTitleTextList:ArrayList<TextView> = ArrayList()
    private var titleIDs:ArrayList<Int> = arrayListOf(R.id.weekreport_tv_title_kcal,R.id.weekreport_tv_title_carb,R.id.weekreport_tv_title_protein,R.id.weekreport_tv_title_fett)
    private var subTitleIDs:ArrayList<Int> = arrayListOf(R.id.weekreport_tv_subtitle_kcal,R.id.weekreport_tv_subtitle_carb,R.id.weekreport_tv_subtitle_protein,R.id.weekreport_tv_subtitle_fett)

    // RecyclerView:
    private lateinit var recyclerView:RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: WeekReportRecyclerViewAdapter







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
        rootView = inflater.inflate(R.layout.dialog_weekreport, container, false)


        dialogLoader = DialogLoading(rootView.context)
        initToolBar()
        initViewElements()
        initCharts()




        reporter = WeekReportCreator(mDate,foodViewModel)
        reporter.getCreated().observe(viewLifecycleOwner, Observer {

            if(it == 0)
            {

                toolbar.subtitle = "${reporter.getFirstDay()} - ${reporter.getLastDay()} "
                initContent()
                initRecyclerView()
                updateChartContent()


                initAnimation()

            }
            else
            {
                newValuesAnimation()

            }

        })






        return rootView
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Init Views
    private fun initToolBar() {
        toolbar = rootView.findViewById(R.id.dialog_weekreport_toolbar)
        //toolbar.collapseIcon = ContextCompat.getDrawable(rootView.context,)
        toolbar.setNavigationOnClickListener {
            // handle back button naviagtion
            dismiss()
        }
        toolbar.inflateMenu(R.menu.menu_weekreport)
        toolbar.setOnMenuItemClickListener{
            initCalendar()
            true
        }

    }

    // InitViews:
    private fun initViewElements()
    {
        // Buttons initialisieren:
        btnNutrition = rootView.findViewById(R.id.weekreport_btn_nutrition)

        // Listener:
        btnNutrition.setOnClickListener(this)


        // TextViews initialisieren:
        for(i in titleIDs)
        {
            titleTextList.add(rootView.findViewById(i))
        }
        for(i in subTitleIDs)
        {
            subTitleTextList.add(rootView.findViewById(i))
        }

        chart = rootView.findViewById(R.id.chart)





    }
    private fun initContent()
    {
        var values = reporter.getCalcedValues()
        for((index,i) in titleTextList.withIndex())
        {
            if(index == 0)
            {
                i.text = "${helper.getFloatAsFormattedString(values[index],"#")} Kcal"
            }
            else
            {
                i.text = "${helper.getFloatAsFormattedString(values[index],"#")} g"
            }
        }

        for((index,i) in subTitleTextList.withIndex())
        {
            if(index == 0)
            {
                i.text = "Ø ${helper.getFloatAsFormattedString(values[index]/7f,"#")} Kcal"
            }
            else
            {
                i.text = "Ø ${helper.getFloatAsFormattedString(values[index]/7f,"#")} g"
            }
        }
    }
    private fun initRecyclerView()
    {
        recyclerView = rootView.findViewById(R.id.weekreport_rv)
        layoutManager = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapter = WeekReportRecyclerViewAdapter(reporter.getWeeklyFoodList(),rootView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }

    private fun initAnimation(delay:Long = 0)
    {
        var alpha = PropertyValuesHolder.ofFloat(View.ALPHA,0f,1f)
        var scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,0f,1f)
        var scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,0f,1f)
        var views:ArrayList<View> = ArrayList()
        views.addAll(titleTextList)
        views.addAll(subTitleTextList)

        var animators:ArrayList<ObjectAnimator> = ArrayList()
        for(i in views)
        {
            var value: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(i,alpha,scaleX,scaleY).apply {
                startDelay = delay
                duration = 500
                interpolator = FastOutSlowInInterpolator()

            }
            animators.add(value)
        }

        var set = AnimatorSet()
        set.playTogether(animators as Collection<Animator>?)
        set.start()
        set.addListener(object:Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                dialogLoader.dismiss()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }

    private fun newValuesAnimation()
    {
        var alpha = PropertyValuesHolder.ofFloat(View.ALPHA,1f,0f)
        var scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,1f,0f)
        var scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,1f,0f)
        var views:ArrayList<View> = ArrayList()
        views.addAll(titleTextList)
        views.addAll(subTitleTextList)

        var animators:ArrayList<ObjectAnimator> = ArrayList()
        for(i in views)
        {
            var value: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(i,alpha,scaleX,scaleY).apply {
                startDelay = 0
                duration = 500
                interpolator = FastOutSlowInInterpolator()

            }
            animators.add(value)
        }

        var set = AnimatorSet()
        set.playTogether(animators as Collection<Animator>?)
        set.start()
        set.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                toolbar.subtitle = "${reporter.getFirstDay()} - ${reporter.getLastDay()} "
                initContent()
                initRecyclerView()
                updateChartContent()
                initAnimation(0)
                dialogLoader.dismiss()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }

        })
    }


    // Init Line Chart
    private fun initCharts()
    {

        var dataSetList:ArrayList<LineDataSet> = ArrayList()
        for(i in 0..3)
        {
            var entries:ArrayList<Entry> = ArrayList()
            for(j in 0..6)
            {
                entries.add(Entry(j.toFloat(),0f))

            }
            dataSetList.add(LineDataSet(entries,""))
        }




        var colors = arrayListOf(
            ContextCompat.getColor(rootView.context,R.color.colorPrimary),
            ContextCompat.getColor(rootView.context,R.color.bar_color_1),
            ContextCompat.getColor(rootView.context,R.color.bar_color_2),
            ContextCompat.getColor(rootView.context,R.color.textColor1))
        var labels = arrayListOf("Kalorien","Kohlenhydrate","Protein","Fett")
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
            if(index == 0) i.axisDependency = YAxis.AxisDependency.LEFT
            else i.axisDependency = YAxis.AxisDependency.RIGHT

        }


        /*// Create Content:
        var entries:ArrayList<Entry> = ArrayList()
        var entries2:ArrayList<Entry> = ArrayList()
        for(i in 0..6)
        {
            entries.add(Entry(i.toFloat(),i*i.toFloat()))
            entries2.add(Entry(i.toFloat(), (i*i*i).toFloat()))
        }

        // LineDataSet
        var dataSet = LineDataSet(entries,"statistic")
        dataSet.color = ContextCompat.getColor(rootView.context,R.color.colorAccent)
        dataSet.valueFormatter = MyValueFormatter()
        dataSet.setDrawFilled(false)
        dataSet.fillColor = ContextCompat.getColor(rootView.context,R.color.colorAccent)
        dataSet.fillAlpha = 50
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.setDrawCircles(false)
        dataSet.lineWidth = 2f
        dataSet.axisDependency = YAxis.AxisDependency.LEFT

        // LineDataSet2:
        var dataSet2 = LineDataSet(entries2,"statistic2")
        dataSet2.color = ContextCompat.getColor(rootView.context,R.color.Error)
        dataSet2.valueFormatter = MyValueFormatter()
        dataSet2.setDrawFilled(false)
        dataSet2.fillColor = ContextCompat.getColor(rootView.context,R.color.Error)
        dataSet2.fillAlpha = 50
        dataSet2.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet2.setDrawCircles(false)
        dataSet2.lineWidth = 2f
        dataSet2.axisDependency = YAxis.AxisDependency.LEFT
*/

        // LineData
        var dataSets:ArrayList<ILineDataSet> = ArrayList()
        for(i in dataSetList) dataSets.add(i)
        /*dataSets.add(dataSet)
        dataSets.add(dataSet2)*/

        var data = LineData(dataSets)
        data.setValueFormatter(MyValueFormatter())
        data.setDrawValues(false)


        chart.data = data
        //chart.legend.isEnabled = true
        chart.description.isEnabled = false

        var legend = chart.legend
        legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER





        var xAxis = chart.xAxis
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MyValueFormatter()


        var yAxisLeft = chart.axisLeft
        var yAxisRight = chart.axisRight
        yAxisLeft.setDrawLabels(true)
        yAxisLeft.setDrawGridLines(false)
        yAxisRight.setDrawGridLines(false)
        yAxisLeft.isEnabled = true
        yAxisRight.isEnabled = true

        yAxisLeft.axisMinimum = 0f
        yAxisRight.axisMinimum = 0f


        yAxisLeft.axisMaximum = 1000f
        yAxisRight.axisMaximum = 100f

        chart.extraBottomOffset = 10f
        chart.invalidate()

    }

    private fun updateChartContent()
    {
        var dataSetList:ArrayList<LineDataSet> = ArrayList()
        for(i in reporter.getChartValues())
        {
            var entries:ArrayList<Entry> = ArrayList()
            for(j in 0..6)
            {
                entries.add(Entry(j.toFloat(),i[j]))

            }
            dataSetList.add(LineDataSet(entries,""))
        }

        var colors = arrayListOf(
            ContextCompat.getColor(rootView.context,R.color.colorPrimary),
            ContextCompat.getColor(rootView.context,R.color.bar_color_1),
            ContextCompat.getColor(rootView.context,R.color.bar_color_2),
            ContextCompat.getColor(rootView.context,R.color.textColor1))
        var labels = arrayListOf("Kalorien","Kohlenhydrate","Protein","Fett")

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
            if(index == 0) i.axisDependency = YAxis.AxisDependency.LEFT
            else i.axisDependency = YAxis.AxisDependency.RIGHT

        }

        var dataSets:ArrayList<ILineDataSet> = ArrayList()
        for(i in dataSetList) dataSets.add(i)
        var data = LineData(dataSets)
        data.setValueFormatter(MyValueFormatter())
        data.setDrawValues(false)

        var yAxisLeft = chart.axisLeft
        var yAxisRight = chart.axisRight
        yAxisRight.resetAxisMaximum()
        yAxisLeft.resetAxisMaximum()

        chart.clearValues()
        chart.data = data
        chart.animateY(500)


        chart.invalidate()

    }

    // Implementierte Methoden:
    override fun onClick(view: View?) {
        var dialog = DialogFragmentWeekReportNutrition(foodViewModel, reporter)
        dialog.show(fragmentManager!!,"nutrition")

    }


    // Calendar starten:
    // Calendar Logik + Objekte initialisieren...
    private fun initCalendar()
    {
        var dialog = DialogDatePicker(rootView.context,rootView)
        dialog.setOnDialogClickListener(object: DialogDatePicker.OnDialogClickListener
        {
            override fun setOnDialogClickListener(date: Date) {
                dialogLoader = DialogLoading(rootView.context)
                reporter.setNewDate(helper.getStringFromDate(date),dialogLoader)


            }

        })



    }




}