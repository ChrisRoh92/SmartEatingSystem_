package de.rohnert.smarteatingsystem.frontend.statistics.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.frontend.statistics.StatisticViewModel

class StatisticBodyFragment: Fragment()
{
    // Allgmeine Variablen:
    private lateinit var rootView: View

    // Content:
    private lateinit var statisticViewModel:StatisticViewModel

    // Views:

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_statistic_body, container, false)

        statisticViewModel = ViewModelProvider(requireActivity(),defaultViewModelProviderFactory).get(StatisticViewModel::class.java)





        return rootView
    }



}