package de.rohnert.smeasy.frontend.settings.fragment

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
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
import de.rohnert.smeasy.helper.dialogs.CustomDatePicker
import de.rohnert.smeasy.helper.dialogs.DialogSingleChoiceList
import de.rohnert.smeasy.helper.dialogs.DialogSingleLineInput
import java.lang.Exception
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

    // PremiumCard
    private lateinit var cardPremium:CardView
    private lateinit var tvPremiumStatus:TextView
    private lateinit var tvPremiumTime:TextView
    private lateinit var tvPremiumDate:TextView




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        // Toolbar bereinigen:
        prefs = SharedAppPreferences(rootView.context)
        initToolBar()
        initPremiumCard()
        initContent()
        initMainCard()
        //initNotificationCard()


        return rootView
    }

    private fun initToolBar()
    {

            // Access to Toolbar.
            var toolbar = activity!!.findViewById<Toolbar>(R.id.toolbar)
            toolbar.menu.clear()
            toolbar.title = "Einstellungen"

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
        for((index,i) in tvIdList.withIndex())
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
        rvNotification.addItemDecoration(DividerItemDecoration(rvNotification.context,layoutManagerMain.orientation))
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

    private fun initPremiumCard()
    {
        cardPremium = rootView.findViewById(R.id.fragment_settings_card_premium)

        var endDate = prefs.premiumEndDate
        var status = prefs.premiumStatus

        Log.d("Smeasy","SettingsFragment - initPremiumCard() - endDate:String = $endDate")
        try {
            Log.d("Smeasy","SettingsFragment - initPremiumCard() - endDate:Date = ${helper.getDateFromString(endDate)}")
        }catch (e:Exception)
        {
            e.printStackTrace()
            Log.d("Smeasy","SettingsFragment - initPremiumCard() - endDate:Date = geht nicht ....")
        }



        // TextView...
        tvPremiumStatus = rootView.findViewById(R.id.settings_tv_premium_state)
        tvPremiumTime = rootView.findViewById(R.id.settings_tv_premium_timerange)
        tvPremiumDate = rootView.findViewById(R.id.settings_tv_premium_enddate)



        if(status)
        {
            tvPremiumStatus.text = "Premium aktiv"
            var restTage = 0
            if(endDate != "")
            {
                restTage = helper.getDaysBetweenDates(helper.getDateFromString(endDate),helper.getCurrentDate()) +1
            }
            else
            {
                restTage = 0
            }

            tvPremiumTime.text = "Noch $restTage übrig"
            tvPremiumDate.text = endDate
        }
        else
        {
            tvPremiumStatus.text = "Premium nicht aktiv"
            tvPremiumTime.text = "-"
            tvPremiumDate.text = "-"
        }
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
                    tvList[1].text = helper.getStringFromDate(bday)
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