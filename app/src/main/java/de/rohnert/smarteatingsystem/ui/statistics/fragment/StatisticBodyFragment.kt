package de.rohnert.smarteatingsystem.ui.statistics.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter

import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.databases.body_database.Body
import de.rohnert.smarteatingsystem.ui.statistics.StatisticViewModel
import de.rohnert.smarteatingsystem.ui.statistics.charts.WeightLineChart
import de.rohnert.smarteatingsystem.utils.Constants.LOGGING_TAG_ANALYSIS
import de.rohnert.smarteatingsystem.utils.datesAreOnSameDay
import de.rohnert.smarteatingsystem.utils.getDateListFromToDate
import de.rohnert.smarteatingsystem.utils.getStringFromDate
import de.rohnert.smarteatingsystem.utils.others.MyCustomValueFormatter
import de.rohnert.smarteatingsystem.utils.standard_charts.SingleLineChart
import de.rohnert.smarteatingsystem.utils.standard_charts.StandardLineChart
import java.util.*
import kotlin.collections.ArrayList


class StatisticBodyFragment: Fragment()
{
    // Allgmeine Variablen:
    private lateinit var rootView: View

    // Content:
    private lateinit var viewModel: StatisticViewModel

    // Charts:
    private lateinit var weightChart: WeightLineChart

    // Views:

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_statistic_body, container, false)


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(),defaultViewModelProviderFactory).get(StatisticViewModel::class.java)
        initWeightLineChart()
        initObservers()

        // Befehl zum Laden der Daten absenden:
        viewModel.loadBodyData()
    }

    private fun initObservers()
    {
        viewModel.getBodyList().observe(viewLifecycleOwner, Observer { bodylist ->
            // val getBodyMap:
            Log.d(LOGGING_TAG_ANALYSIS,"Anzahl Einträge in BodyList: ${bodylist.size}")
            val bodyMap = getUniqueBodyMap(bodylist)
            // Datelist erstellen:
            val dateList = getDateListFromToDate(Date(bodylist[0].date), Date(bodylist.last().date))

            weightChart.updateChart(bodyMap,dateList)



        })

    }

    private fun initWeightLineChart()
    {
/*        val xValues2 = ArrayList<String>()
        for(i in 1..30)
        {
            xValues2.add("$i.07")
        }

        val xValues = arrayListOf("15.08.21","01.09.21","15.09.21","01.10.21")
        val yValues = arrayListOf(98.0,97.3,96.5,95.8,95.3)*/

        weightChart = WeightLineChart(R.id.statistic_body_chart_kcal,rootView, mapOf<Int,Body>(),ArrayList())
        weightChart.init()


    }


    private fun getUniqueBodyMap(bodyList:ArrayList<Body>):Map<Int, Body> {
        // Map erstellen für Bodyeinträge mit Indices:
        val bodyMap = mutableMapOf<Int, Body>()

        // Start und End Datum feststellen....
        val fromDate = Date(bodyList[0].date)
        val toDate = Date(bodyList.last().date)
        Log.d(LOGGING_TAG_ANALYSIS,"fromDate: $fromDate")
        Log.d(LOGGING_TAG_ANALYSIS,"toDate: $toDate")


        // Liste von Dates zwischen 1. und letzten Date generieren:
//        Log.d(LOGGING_TAG_ANALYSIS,"dayList wird erstellt")
        val dayList = getDateListFromToDate(fromDate, toDate)
//        Log.d(LOGGING_TAG_ANALYSIS,"Anzahl Einträge in dayList: ${dayList.size}")
        // Durch übergebene Body durchiterien:
//        var currentDate = fromDate
        for (body: Body in bodyList) {
            // Prüfen, welcher Index das jeweilige Date von bodyList in dayList hat:
            for (i in dayList.indices) {
//                Log.d(LOGGING_TAG_ANALYSIS,"Index in For-Schleife: $i von ${dayList.size}")
                if (datesAreOnSameDay(Date(body.date), dayList[i])) {
                    bodyMap[i] = body
//                    currentDate = Date(body.date)
                    break
                }
            }

//            Log.d(LOGGING_TAG_ANALYSIS,"Anzahl Einträge in bodyMap: ${bodyMap.size}")



        }

        return bodyMap
    }





}