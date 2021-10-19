package de.rohnert.smarteatingsystem.frontend.settings.fragment

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.frontend.settings.adapter.NotificationSettingsAdapter
import de.rohnert.smarteatingsystem.utils.dialogs.CustomDatePicker
import de.rohnert.smarteatingsystem.utils.dialogs.DialogSingleChoiceList
import de.rohnert.smarteatingsystem.utils.dialogs.DialogSingleLineInput
import java.util.*
import kotlin.collections.ArrayList


class SettingsFragment : Fragment(), View.OnClickListener {



    // Allgemeine Variablen
    private lateinit var rootView:View
    private var helper = Helper()

    // private Content:
    private lateinit var prefs:SharedAppPreferences
    // Main
    private  var tvList:ArrayList<TextView> = ArrayList()
    private  var itemList:ArrayList<View> = ArrayList()
    private var itemIdList:ArrayList<Int> = arrayListOf(R.id.settings_item_bday,R.id.settings_item_sex,R.id.settings_item_userheight,R.id.settings_item_username)
    private var tvIdList:ArrayList<Int> = arrayListOf(R.id.settings_tv_bday_value,R.id.settings_tv_sex_value,R.id.settings_tv_userheight_value,R.id.settings_tv_username_value)
    private lateinit var mainContent:ArrayList<String>

    // Notification FoodTracker TODO: Das muss auch von den Prefs kommen!
    private var notificationFoodContent:ArrayList<String> = arrayListOf("Frühstück","Mittagessen","Abendessen","Snacks")
    private var notificationFoodSubContent:ArrayList<String> = arrayListOf("09:00","12:00","18:00","15:00")
    private var notificationFoodCheckContent:ArrayList<Boolean> = arrayListOf(true,true,true,true)


    private lateinit var rvFoodNotification:RecyclerView
    private lateinit var adapterFoodNotification: NotificationSettingsAdapter
    private lateinit var switchFoodNotification:Switch


    // Notification FoodTracker
    private var notificationBodyContent:ArrayList<String> = arrayListOf("Morgens","Mittags","Abends")
    private var notificationBodySubContent:ArrayList<String> = arrayListOf("09:00","12:00","20:00")
    private var notificationBodyCheckContent:ArrayList<Boolean> = arrayListOf(true,true,true)

    private lateinit var rvBodyNotification:RecyclerView
    private lateinit var adapterBodyNotification: NotificationSettingsAdapter
    private lateinit var switchBodyNotification:Switch





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView = view

        prefs = SharedAppPreferences(rootView.context)
        initToolBar()

        initContent()
        initMainCard()
        initNotificationFoodCard()
        initNotificationBodyCard()

    }

    private fun initToolBar()
    {

            // Access to Toolbar.
            var toolbar = activity!!.findViewById<Toolbar>(R.id.toolbar)
            toolbar.menu.clear()
            toolbar.title = "Einstellungen"
            toolbar.subtitle = ""

    }

    private fun initContent()
    {
        prefs = SharedAppPreferences(rootView.context)
        mainContent = ArrayList()
        mainContent.add(prefs.bday)
        mainContent.add(prefs.sex)
        mainContent.add("${helper.getFloatAsFormattedString(prefs.userHeight,"#")} cm")
        mainContent.add(prefs.userName)
    }

    private fun initMainCard()
    {
        // TextViews initialisieren...
        for(i in tvIdList)
        {
            tvList.add(rootView.findViewById(i))
        }

        for((index,i) in tvList.withIndex())
        {
            i.text = mainContent[index]
        }

        // ItemClicks initialisieren:
        for(i in itemIdList)
        {
            itemList.add(rootView.findViewById(i))
        }

        for(i in itemList)
        {
            i.setOnClickListener(this)
        }


    }



    private fun initNotificationFoodCard()
    {

        // Switch:
        switchFoodNotification = rootView.findViewById(R.id.fragment_settings_switch_notification_food)
        switchFoodNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
            Toast.makeText(requireContext(),"Alarm ist nun Aktiv",Toast.LENGTH_SHORT).show()
            else
            Toast.makeText(requireContext(),"Alarm ist nun NICHT Aktiv",Toast.LENGTH_SHORT).show()
        }



        // RecyclerView initialisieren:
        rvFoodNotification = rootView.findViewById(R.id.fragment_settings_rv_notification_food)
        rvFoodNotification.layoutManager = LinearLayoutManager(rvFoodNotification.context,RecyclerView.VERTICAL,false)
        adapterFoodNotification = NotificationSettingsAdapter(notificationFoodContent,notificationFoodSubContent,notificationFoodCheckContent)
        rvFoodNotification.adapter = adapterFoodNotification

        // Click Listener:
        adapterFoodNotification.setOnNotificationCheckBoxListener(object:NotificationSettingsAdapter.OnNotificationCheckBoxListener{
            override fun setOnNotificationCheckBoxListener(pos: Int) {
                // TODO("Not yet implemented")
            }

        })
        // Click Listener:
        adapterFoodNotification.setOnNotificationSettingsClickListener(object:NotificationSettingsAdapter.OnNotificationSettingsClickListener{
           override fun setOnNotificationSettingsClickListener(pos: Int) {
                // TODO("Not yet implemented")
            }

        })


    }

    private fun initNotificationBodyCard()
    {
        // Switch:
        switchBodyNotification = rootView.findViewById(R.id.fragment_settings_switch_notification_Body)
        switchBodyNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked)
                Toast.makeText(requireContext(),"Alarm ist nun Aktiv",Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(requireContext(),"Alarm ist nun NICHT Aktiv",Toast.LENGTH_SHORT).show()
        }



        // RecyclerView initialisieren:
        rvBodyNotification = rootView.findViewById(R.id.fragment_settings_rv_notification_body)
        rvBodyNotification.layoutManager = LinearLayoutManager(rvBodyNotification.context,RecyclerView.VERTICAL,false)
        adapterBodyNotification = NotificationSettingsAdapter(notificationBodyContent,notificationBodySubContent,notificationBodyCheckContent)
        rvBodyNotification.adapter = adapterBodyNotification

        // Click Listener:
        adapterBodyNotification.setOnNotificationCheckBoxListener(object:NotificationSettingsAdapter.OnNotificationCheckBoxListener{
            override fun setOnNotificationCheckBoxListener(pos: Int) {
                // TODO("Not yet implemented")
            }

        })
        // Click Listener:
        adapterBodyNotification.setOnNotificationSettingsClickListener(object:NotificationSettingsAdapter.OnNotificationSettingsClickListener{
            override fun setOnNotificationSettingsClickListener(pos: Int) {
                // TODO("Not yet implemented")
            }

        })

    }




    // Implementierte Methoden:
    override fun onClick(v: View?) {
        if(v == itemList[0])
        {
            // Geburtstagsdialog starten
            var bday = helper.getDateFromString(mainContent[0])
            var dialog = CustomDatePicker(bday)
            dialog.show(fragmentManager!!,"DatePicker")
            dialog.setOnCustomDatePickerListener(object:CustomDatePicker.OnCustomDatePickerListener{
                override fun setOnCustomDatePickerListener(date: Date) {
                    bday = date
                    mainContent[0] = helper.getStringFromDate(bday)
                    prefs.setNewBday(mainContent[0])
                    tvList[0].text = helper.getStringFromDate(bday)
                }

            })
        }
        else if (v == itemList[1])
        {
            var content:ArrayList<String> = arrayListOf("Frau","Mann")
            var pos = 0
            pos = content.indexOf(mainContent[1])
            var dialog = DialogSingleChoiceList("Geschlecht angeben","",content,rootView.context,true,pos)
            dialog.onItemClickListener(object:DialogSingleChoiceList.OnDialogListListener{
                override fun onItemClickListener(value: String, pos: Int) {
                    mainContent[1] = content[pos]
                    prefs.setNewSex(content[pos])
                    tvList[1].text = content[pos]

                }

            })

        }
        else if (v == itemList[2])
        {
            var dialog = DialogSingleLineInput("Deinen Körpergröße angeben","Angabe in cm",rootView.context,InputType.TYPE_NUMBER_FLAG_DECIMAL,helper.getFloatAsFormattedString(prefs.userHeight,"#"))
            dialog.onDialogClickListener(object :DialogSingleLineInput.OnDialogListener{
                override fun onDialogClickListener(export: String)
                {
                    Log.d("Smeasy","SettingsFragment - Userheight Dialog : export:String = $export")

                }

                override fun onDialogClickListener(export: Float) {
                    Log.d("Smeasy","SettingsFragment - Userheight Dialog : export:Float = $export")
                    mainContent[2] = "${helper.getFloatAsFormattedString(export,"#")} cm"
                    tvList[2].text = "${helper.getFloatAsFormattedString(export,"#")} cm"
                    prefs.setNewUserHeight(export)
                }

            })
        }
        else if (v == itemList[3])
        {
            var dialog = DialogSingleLineInput("Deinen Namen angeben","",rootView.context,InputType.TYPE_CLASS_TEXT,mainContent[3])
            dialog.onDialogClickListener(object :DialogSingleLineInput.OnDialogListener{
                override fun onDialogClickListener(export: String) {
                    mainContent[3] = export
                    tvList[3].text = mainContent[3]
                    prefs.setNewUserName(export)
                }

                override fun onDialogClickListener(export: Float) {

                }

            })
        }
    }


}