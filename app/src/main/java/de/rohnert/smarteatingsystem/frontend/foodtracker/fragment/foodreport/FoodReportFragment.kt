package de.rohnert.smarteatingsystem.frontend.foodtracker.fragment.foodreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel.FoodViewModel
import kotlinx.android.synthetic.main.app_bar_main.*

class FoodReportFragment(): Fragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView:View
    private val TAG = "Smeasy"

    // View Elemente:




    // ViewModel:
    private lateinit var foodViewModel: FoodViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_foodreport,container,false)

        foodViewModel = ViewModelProvider(requireParentFragment()).get(FoodViewModel::class.java)
        initToolbar()

        return rootView
    }


    private fun initToolbar()
    {
        val toolbar = requireActivity().toolbar
        toolbar.menu.clear()
        toolbar.title = "Wochenbericht"
    }
}