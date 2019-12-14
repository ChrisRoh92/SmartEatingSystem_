package de.rohnert.smeasy.frontend.statistics.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.bodytracker.BodyViewModel
import de.rohnert.smeasy.frontend.bodytracker.adapter.BodyTrackerPagerAdapter
import de.rohnert.smeasy.frontend.statistics.StatisticViewModel
import de.rohnert.smeasy.frontend.statistics.adapter.StatisticPagerAdapter
import de.rohnert.smeasy.frontend.statistics.dialogs.DialogStatisticsTimeChooser
import de.rohnert.smeasy.helper.dialogs.DialogLoading
import android.content.Intent
import android.net.Uri
import java.io.File
import androidx.core.content.FileProvider





class StatisticsFragment: Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var statisticViewModel: StatisticViewModel

    // View Elemente:
    private lateinit var toolbar:Toolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var pager: ViewPager
    private lateinit var adapter: StatisticPagerAdapter
    private lateinit var btnTimeRange: Button

    // Content:
    private var timeValue ="Letzte 7 Tage"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        statisticViewModel = ViewModelProviders.of(this).get(StatisticViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_statistics, container, false)

        initViewPager()
        initToolbar()
        initButton()

        return rootView
    }

    // Toolbar:
    private fun initToolbar()
    {
        // Access to Toolbar.
        toolbar = activity!!.findViewById<Toolbar>(R.id.toolbar)
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.menu_statistic)
        toolbar.setOnMenuItemClickListener(object: Toolbar.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                if(item!!.itemId == R.id.nav_statistic_export)
                {
                    Toast.makeText(rootView.context,"Datenexport wird wird gestartet",Toast.LENGTH_SHORT).show()
                    startCSVExportProess()

                }
                return true
            }

        })

        toolbar.title = "Statistik"
        toolbar.subtitle ="Zeitraum: Letzte 7 Tage"

        /*var item2 = toolbar.menu.findItem(R.id.nav_statistics_spinner)
        var spinner = item2.actionView as Spinner
        var spinnerContent:ArrayList<String> = arrayListOf("Wochenansicht","Monatsansicht","Letzte 3 Monate","Letztes Jahr","Benutzerdefiniert")
        var adapter = ArrayAdapter<String>(rootView.context,android.R.layout.simple_spinner_dropdown_item,spinnerContent)
        spinner.adapter = adapter*/

    }

    private fun startCSVExportProess()
    {

        //var dialog = DialogLoading(rootView.context)

        statisticViewModel.getCSVExportFinished().observe(viewLifecycleOwner, Observer {
            //dialog.dismiss()
            var fileName = statisticViewModel.getCSVExportFileName()
            startShareIntentProcess(fileName)


        })




        statisticViewModel.startCSVExport()
    }

    private fun startShareIntentProcess(fileName:String)
    {


        val file = File(fileName)
        val path =
            FileProvider.getUriForFile(rootView.context, "de.rohnert.smeasy.fileprovider", file)
        val uri = Uri.fromFile(file)
        val emailIntent = Intent(Intent.ACTION_SEND)

        emailIntent.type = "text/csv"
        /*emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "")
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri)*/

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "data");
        emailIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        startActivity(Intent.createChooser(emailIntent, "Email senden"))


    }

    private fun initViewPager()
    {
        // View initialisieren:
        tabLayout = rootView.findViewById(R.id.statistic_tablayout)
        tabLayout!!.addTab(tabLayout!!.newTab().setText("FoodTracker"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("BodyTracker"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


        pager = rootView.findViewById(R.id.statistic_viewpager)
        adapter = StatisticPagerAdapter(fragmentManager!!)
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

    private fun initButton()
    {
        btnTimeRange = rootView.findViewById(R.id.statistic_btn_time_range)
        btnTimeRange.setOnClickListener {
            var dialog = DialogStatisticsTimeChooser(rootView.context,timeValue)
            dialog.setOnDialogSaveClickListener(object:DialogStatisticsTimeChooser.OnDialogSaveClickListener{
                override fun setOnDialogSaveClickListener(value: String) {
                    timeValue = value
                    toolbar.subtitle = "Zeitraum: $value"
                }

            })

        }
    }


}