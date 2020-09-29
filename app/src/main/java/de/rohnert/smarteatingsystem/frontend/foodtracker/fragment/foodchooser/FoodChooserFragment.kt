package de.rohnert.smarteatingsystem.frontend.foodtracker.fragment.foodchooser

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.frontend.foodtracker.adapter.FoodChooserViewPager2Adapter
import de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel.FoodViewModel
import de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel.TAG
import kotlinx.android.synthetic.main.app_bar_main.*

class FoodChooserFragment():Fragment() {


    private lateinit var rootView: View

    // ViewPager2
    private lateinit var viewpager2:ViewPager2
    private lateinit var adapter:FoodChooserViewPager2Adapter
    private lateinit var tabLayout: TabLayout

    // Values:
    private var sMeal:String? = null

    // ViewModel
    private lateinit var foodViewModel:FoodViewModel






    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_foodchooser,container,false)

        sMeal = requireArguments()?.getString("sMeal","breakfast")
        foodViewModel = ViewModelProvider(requireActivity()).get(FoodViewModel::class.java)
        foodViewModel.sMeal = sMeal ?: ""


        initTablayout()

        initToolBar()



        return rootView
    }


    private fun initToolBar()
    {
        var toolbar = requireActivity().toolbar
        toolbar.title = "Lebensmittelauswahl..."
        toolbar.menu.clear()
        toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.backgroundLight2))

        toolbar.inflateMenu(R.menu.menu_foodchooser)
    }


    private fun initTablayout()
    {
        viewpager2 = rootView.findViewById(R.id.foodchooser_viewpager2)
        adapter = FoodChooserViewPager2Adapter(parentFragmentManager,lifecycle)
        viewpager2.adapter = adapter

        tabLayout = rootView.findViewById(R.id.foodchooser_tablayout)
        val name = arrayOf("Lebensmittel","Rezepte")
        TabLayoutMediator(tabLayout,viewpager2){tab, position ->
            tab.text = name[position]
        }.attach()
    }

    override fun onDestroyView() {
        Log.d(TAG,"FoodChooserFragment onDestroyView()")
        foodViewModel.searchInFoodList("")
        super.onDestroyView()
    }

















}