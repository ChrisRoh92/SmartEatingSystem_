package de.rohnert.smarteatingsystem.ui.statistics.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter

import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.ui.statistics.StatisticViewModel
import de.rohnert.smarteatingsystem.utils.others.MyCustomValueFormatter
import de.rohnert.smarteatingsystem.utils.standard_charts.StandardLineChart


class StatisticBodyFragment: Fragment()
{
    // Allgmeine Variablen:
    private lateinit var rootView: View

    // Content:
    private lateinit var statisticViewModel: StatisticViewModel

    // Charts:
    private lateinit var weightChart: StandardLineChart

    // Views:

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_statistic_body, container, false)


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statisticViewModel = ViewModelProvider(requireActivity(),defaultViewModelProviderFactory).get(StatisticViewModel::class.java)
        initWeightLineChart()
    }

    private fun initWeightLineChart()
    {
        val labels = arrayListOf("01.08.21","15.08.21","01.09.21","15.09.21","01.10.21")
        val values = arrayListOf(98.0,97.3,96.5,95.8,95.3)

        weightChart = StandardLineChart(R.id.statistic_body_chart_kcal,rootView,values,labels)


    }



}