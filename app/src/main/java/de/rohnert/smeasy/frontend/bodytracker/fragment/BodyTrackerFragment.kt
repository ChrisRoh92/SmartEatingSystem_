package de.rohnert.smeasy.frontend.bodytracker.fragment


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.bodytracker.adapter.BodyTrackerPagerAdapter
import de.rohnert.smeasy.frontend.bodytracker.BodyViewModel
import de.rohnert.smeasy.frontend.foodtracker.dialogs.DialogFragmentWeekReport

class BodyTrackerFragment: Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var bodyViewModel: BodyViewModel

    // View Elemente:
    private lateinit var tabLayout:TabLayout
    private lateinit var pager:ViewPager
    private lateinit var adapter: BodyTrackerPagerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bodyViewModel = ViewModelProviders.of(this).get(BodyViewModel::class.java)
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
        var toolbar = activity!!.findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.clear()
        /*toolbar.inflateMenu(R.menu.menu_bodytracker)
        toolbar.setOnMenuItemClickListener(object: Toolbar.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                if(item!!.itemId == R.id.menu_bodytracker_report)
                {

                }
                return true
            }

        })*/
    }

    private fun initViewPager()
    {
        // View initialisieren:
        tabLayout = rootView.findViewById(R.id.bodytracker_tablayout)
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Body Einträge"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Body Einstellungen"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


        pager = rootView.findViewById(R.id.bodytracker_viewpager)
        adapter = BodyTrackerPagerAdapter(fragmentManager!!)
        pager.adapter = adapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener
        {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                pager.currentItem = p0!!.position
            }

        })




    }


}