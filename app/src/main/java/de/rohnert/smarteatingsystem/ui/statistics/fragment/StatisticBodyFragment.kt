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
import de.rohnert.smarteatingsystem.ui.statistics.StatisticViewModel
import de.rohnert.smarteatingsystem.utils.Constants.LOGGING_TAG_ANALYSIS
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
    private lateinit var weightChart: StandardLineChart

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
            // Daten in das LineChart geben:
            val weights = ArrayList<Double>()
            val dates = ArrayList<String>()
            val firstDate = bodylist[0].date
            val lastDate = bodylist.last().date
            for(i in bodylist)
            {
                weights.add(i.weight.toDouble())
                dates.add(getStringFromDate(Date(i.date),"dd/MM"))

            }

            Log.d(LOGGING_TAG_ANALYSIS,"Num. of Entries of Weights: ${weights.size}")
            Log.d(LOGGING_TAG_ANALYSIS,"Num. of Entries of Dates: ${dates.size}")

            //weightChart.updateLineChart(weights,dates)
            Toast.makeText(requireContext(),"Daten werden aktualisiert...",Toast.LENGTH_SHORT).show()

        })

    }

    private fun initWeightLineChart()
    {
        val xValues2 = ArrayList<String>()
        for(i in 1..30)
        {
            xValues2.add("$i.07")
        }

        val xValues = arrayListOf("15.08.21","01.09.21","15.09.21","01.10.21")
        val yValues = arrayListOf(98.0,97.3,96.5,95.8,95.3)

        weightChart = StandardLineChart(R.id.statistic_body_chart_kcal,rootView,
            yValues, xValues2,yIndices = arrayListOf(0f,6f,13f,20f,27f)
        )

    }





}