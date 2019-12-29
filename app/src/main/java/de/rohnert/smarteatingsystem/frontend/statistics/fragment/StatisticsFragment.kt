package de.rohnert.smarteatingsystem.frontend.statistics.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.frontend.statistics.StatisticViewModel
import de.rohnert.smarteatingsystem.frontend.statistics.adapter.StatisticPagerAdapter
import de.rohnert.smarteatingsystem.frontend.statistics.dialogs.DialogStatisticsTimeChooser
import android.content.Intent
import android.util.Log
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
        toolbar = activity!!.findViewById(R.id.toolbar)
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
            var file = statisticViewModel.returnNewCSVFile()
            Log.d("Smeasy","StatisticsFragment startCSVExportProess - filename = $file")
            startShareIntentProcess(file!!)


        })




        statisticViewModel.startCSVExport()
    }

    private fun startShareIntentProcess(file:File)
    {

        /*var privateRootDir = rootView.context.filesDir.absoluteFile
        // Get the files/images subdirectory;
        var imagesDir = File(privateRootDir, "data")
        // Get the files in the images subdirectory
        var imageFiles:Array<File> = imagesDir.listFiles()*/

        /*val requestFile:File = File(fileName)
        var resultIntent = Intent("de.rohnert.smeasy.ACTION_RETURN_FILE")
        activity!!.setResult(RESULT_CANCELED, null)

        val fileUri: Uri? = try {
            FileProvider.getUriForFile(
                rootView.context,
                "de.rohnert.smeasy.fileprovider",
                requestFile)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Log.d("Smeasy",
                "StatisticsFragment - startShareIntentProcess Exception: The selected file can't be shared: $requestFile")
            null
        }

        if (fileUri != null) {
            // Grant temporary read permission to the content URI
            resultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // Put the Uri and MIME type in the result Intent
            resultIntent.setDataAndType(fileUri, activity!!.contentResolver.getType(fileUri))
            // Set the result
            activity!!.setResult(Activity.RESULT_OK, resultIntent)
        } else {
            resultIntent.setDataAndType(null, "")
            activity!!.setResult(RESULT_CANCELED, resultIntent)
        }*/
        Log.d("Smeasy","StatisticsFragment startCSVExportProess - filename = $file")
        var path = FileProvider.getUriForFile(rootView.context,"de.rohnert.smeasy.fileprovider",file)
        var shareIntent = Intent()
        Log.d("Smeasy","StatisticsFragment startCSVExportProess - path = $path")
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Das ist mein CSV Export der letzten 7 Tage bei Smeasy")
        shareIntent.putExtra(Intent.EXTRA_STREAM,path)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        //shareIntent.type =
        shareIntent.setDataAndType(path, rootView.context.contentResolver.getType(path))
        //activity!!.setResult(Activity.RESULT_OK, shareIntent)
        startActivity(Intent.createChooser(shareIntent,""))




        /*val path =
            FileProvider.getUriForFile(rootView.context, "de.rohnert.smeasy.fileprovider", file)
        val uri = Uri.fromFile(file)
        val emailIntent = Intent(Intent.ACTION_SEND)

        emailIntent.type = "text/plain"
        *//*emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "")
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri)*//*

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "data");
        emailIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        startActivity(Intent.createChooser(emailIntent, "Email senden"))*/


    }

    private fun initViewPager()
    {
        // View initialisieren:
        tabLayout = rootView.findViewById(R.id.statistic_tablayout)
        tabLayout.addTab(tabLayout.newTab().setText("FoodTracker"))
        tabLayout.addTab(tabLayout.newTab().setText("BodyTracker"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL


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