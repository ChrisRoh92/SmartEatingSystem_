package de.rohnert.smeasy.frontend.statistics.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import backend.helper.Helper
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.statistics.StatisticViewModel

class StatisticBodyFragment: Fragment()
{
    // Allgmeine Variablen:
    private lateinit var rootView: View
    private var helper = Helper()

    // Content:
    private lateinit var statisticViewModel:StatisticViewModel

    // Views:

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_statistic_body, container, false)

        statisticViewModel = ViewModelProviders.of(this).get(StatisticViewModel::class.java)





        return rootView
    }



}