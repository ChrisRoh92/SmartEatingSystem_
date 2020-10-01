package de.rohnert.smarteatingsystem.frontend.bodytracker.fragment


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.frontend.bodytracker.adapter.BodyTrackerPagerAdapter
import de.rohnert.smarteatingsystem.frontend.bodytracker.BodyViewModel
import kotlinx.android.synthetic.main.app_bar_main.*

class BodyTrackerFragment: Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var bodyViewModel: BodyViewModel

    // View Elemente:
    private lateinit var tabLayout:TabLayout
    private lateinit var pager: ViewPager2
    private lateinit var adapter: BodyTrackerPagerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bodyViewModel = ViewModelProvider(this).get(BodyViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_bodytracker, container, false)

        //
        initToolbar()
        Handler().postDelayed({
            initViewPager()

        },100)


        return rootView
    }

    // Toolbar:
    private fun initToolbar()
    {
        // Access to Toolbar.
        var toolbar = activity!!.toolbar
        toolbar.menu.clear()
        toolbar.title = "Körpertracking"

    }


    private fun initViewPager()
    {
        // View initialisieren:




        pager = rootView.findViewById(R.id.bodytracker_viewpager)
        adapter = BodyTrackerPagerAdapter(parentFragmentManager,lifecycle)
        pager.adapter = adapter

        tabLayout = rootView.findViewById(R.id.bodytracker_tablayout)
        val name = arrayOf("Einträge","Einstellungen")
        TabLayoutMediator(tabLayout,pager){tab, position ->
            tab.text = name[position]
        }.attach()


    }


}