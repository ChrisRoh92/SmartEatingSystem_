package de.rohnert.smarteatingsystem.ui.bodytracker.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.ui.bodytracker.adapter.BodyTrackerPagerAdapter
import de.rohnert.smarteatingsystem.ui.bodytracker.BodyViewModel

class BodyTrackerFragment: Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var bodyViewModel: BodyViewModel

    // View Elemente:
    private lateinit var tabLayout:TabLayout
    private lateinit var pager: ViewPager2
    private lateinit var adapter: BodyTrackerPagerAdapter

    private lateinit var toolbar:Toolbar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_bodytracker, container, false)


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bodyViewModel = ViewModelProvider(this).get(BodyViewModel::class.java)
        initToolbar()
        initViewPager()
    }

    // Toolbar:
    private fun initToolbar()
    {
        // Access to Toolbar.
        toolbar = requireActivity()!!.findViewById(R.id.toolbar)

        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.menu_bodytracker)

        toolbar.title = "Körpertracking"
        toolbar.subtitle = ""

    }


    private fun initViewPager()
    {
        // View initialisieren:




        pager = rootView.findViewById(R.id.bodytracker_viewpager)
        adapter = BodyTrackerPagerAdapter(parentFragmentManager,lifecycle)
        pager.offscreenPageLimit = 2
        pager.adapter = adapter

        tabLayout = rootView.findViewById(R.id.bodytracker_tablayout)
        val name = arrayOf("Einträge","Einstellungen")
        TabLayoutMediator(tabLayout,pager){tab, position ->
            tab.text = name[position]
        }.attach()




    }


}