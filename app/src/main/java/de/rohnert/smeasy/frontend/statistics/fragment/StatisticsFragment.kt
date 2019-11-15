package de.rohnert.smeasy.frontend.statistics.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProviders
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.bodytracker.BodyViewModel
import de.rohnert.smeasy.frontend.statistics.StatisticViewModel

class StatisticsFragment: Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var statisticViewModel: StatisticViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        statisticViewModel = ViewModelProviders.of(this).get(StatisticViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_statistics, container, false)

        return rootView
    }


}