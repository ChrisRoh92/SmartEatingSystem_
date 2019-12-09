package de.rohnert.smeasy.frontend.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import backend.helper.Helper
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.frontend.settings.adapter.MainSettingsAdapter
import de.rohnert.smeasy.frontend.settings.adapter.NotificationSettingsAdapter
import androidx.recyclerview.widget.DividerItemDecoration




class SettingsFragment : Fragment() {


    // Allgemeine Variablen
    private lateinit var rootView:View
    private var helper = Helper()

    // private Content:
    private lateinit var prefs:SharedAppPreferences
    // Main
    private var mainContent:ArrayList<String> = arrayListOf("Name","Deine Größe","Geschlecht","Geburtstag")
    private lateinit var mainSubContent:ArrayList<String>
    // Notification
    private var notificationContent:ArrayList<String> = arrayListOf("Frühstück","Mittagessen","Abendessen","Snacks")
    private var notificationSubContent:ArrayList<String> = arrayListOf("09:00","12:00","18:00","15:00")
    private var notificationCheckContent:ArrayList<Boolean> = arrayListOf(true,true,true,true)

    // View Elemente:
    private lateinit var rvMain:RecyclerView
    private lateinit var layoutManagerMain:LinearLayoutManager
    private lateinit var adapterMain: MainSettingsAdapter

    private lateinit var rvNotification:RecyclerView
    private lateinit var layoutManagerNotification:LinearLayoutManager
    private lateinit var adapterNotification: NotificationSettingsAdapter
    private lateinit var switchNotification:Switch



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        initContent()
        initMainCard()
        initNotificationCard()


        return rootView
    }

    private fun initContent()
    {
        prefs = SharedAppPreferences(rootView.context)
        mainSubContent = ArrayList()
        mainSubContent.add(prefs.userName)
        mainSubContent.add("${helper.getFloatAsFormattedString(prefs.userHeight,"#")} cm")
        mainSubContent.add(prefs.sex)
        mainSubContent.add(prefs.bday)
    }

    private fun initMainCard()
    {
        rvMain = rootView.findViewById(R.id.fragment_settings_rv_main)
        layoutManagerMain = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapterMain = MainSettingsAdapter(mainContent,mainSubContent)
        rvMain.layoutManager = layoutManagerMain
        rvMain.adapter = adapterMain
        rvMain.addItemDecoration(DividerItemDecoration(rvMain.context,layoutManagerMain.orientation))
        adapterMain.setOnMainSettingsClickListener(object: MainSettingsAdapter.OnMainSettingsClickListener{
            override fun setOnMainSettingsClickListener(pos: Int)
            {

            }

        })


    }

    private fun initNotificationCard()
    {
        switchNotification = rootView.findViewById(R.id.fragment_settings_switch_notification)
        switchNotification.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

            }

        })

        rvNotification = rootView.findViewById(R.id.fragment_settings_rv_notification)
        layoutManagerNotification = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapterNotification = NotificationSettingsAdapter(notificationContent,notificationSubContent,notificationCheckContent)
        rvNotification.layoutManager = layoutManagerNotification
        rvNotification.adapter = adapterNotification
        rvNotification.addItemDecoration(DividerItemDecoration(rvMain.context,layoutManagerMain.orientation))
        adapterNotification.setOnNotificationSettingsClickListener(object: NotificationSettingsAdapter.OnNotificationSettingsClickListener{
            override fun setOnNotificationSettingsClickListener(pos: Int) {
                if(switchNotification.isChecked)
                {

                }
                else
                {
                    // Im Adapter die Sachen ausgrauen
                }
            }


        })

        adapterNotification.setOnNotificationCheckBoxListener(object:NotificationSettingsAdapter.OnNotificationCheckBoxListener{
            override fun setOnNotificationCheckBoxListener(pos: Int) {
                // In die SharedPreferences eintragen, ob für die jeweilige Mahlzeit eine Notification gesendet werden soll
            }

        })
    }


}