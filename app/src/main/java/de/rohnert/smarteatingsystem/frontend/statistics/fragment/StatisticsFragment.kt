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
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator


class StatisticsFragment: Fragment() {

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var statisticViewModel: StatisticViewModel

    // View Elemente:
    private lateinit var toolbar:Toolbar

    // ViewPager2:
    private lateinit var tabLayout: TabLayout
    private lateinit var pager: ViewPager2
    private lateinit var adapter: StatisticPagerAdapter


    // Content:
    private var timeValue ="Letzte 7 Tage"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statisticViewModel = ViewModelProvider(requireActivity(),defaultViewModelProviderFactory).get(StatisticViewModel::class.java)

        rootView = view

        initViewPager2()
        initToolbar()



    }

    // Toolbar:
    private fun initToolbar()
    {
        // Access to Toolbar.
        toolbar = activity!!.findViewById(R.id.toolbar)
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.menu_statistic)

        toolbar.setOnMenuItemClickListener { menuItem ->

            if(menuItem.itemId == R.id.nav_statistic_share)
            {
                shareData()
            }
            else if(menuItem.itemId == R.id.nav_statistic_time)
            {
                setTimeRange()
            }


            true
        }

        toolbar.title = "Statistik"
        toolbar.subtitle ="Zeitraum: Letzte 7 Tage"

    }



    private fun initViewPager2()
    {
        pager = rootView.findViewById(R.id.statistic_viewpager2)
        adapter = StatisticPagerAdapter(parentFragmentManager,lifecycle)
        pager.adapter = adapter


        tabLayout = rootView.findViewById(R.id.statistic_tablayout)
        var names = arrayOf("Lebensmittel","Körper")
        TabLayoutMediator(tabLayout,pager){tab, position ->
            tab.text = names[position]
        }.attach()






    }

    private fun setTimeRange()
    {
        var dialog = DialogStatisticsTimeChooser(rootView.context,timeValue)
        dialog.setOnDialogSaveClickListener(object:DialogStatisticsTimeChooser.OnDialogSaveClickListener{
            override fun setOnDialogSaveClickListener(value: String) {
                timeValue = value
                toolbar.subtitle = "Zeitraum: $value"
            }

        })
    }

    private fun shareData()
    {
        Toast.makeText(requireContext(),"Funktion nicht implementiert!",Toast.LENGTH_SHORT).show()
        // TODO: Teilen von Daten implementieren
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


}


