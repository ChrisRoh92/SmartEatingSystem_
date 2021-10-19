package de.rohnert.smarteatingsystem.frontend.bodytracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.utils.dialogs.DialogSingleList

class DialogAutoCalcKcal(var context:Context) : View.OnClickListener,
    AdapterView.OnItemClickListener {



    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private lateinit var prefs: SharedAppPreferences

    // Interface:
    private lateinit var mListener:OnDialogAutoCalcKcalListener

    // View Elemente:
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button
    private lateinit var btnInfo:ImageButton
    private lateinit var listView:ListView

    // Content:
    private var content:ArrayList<String> = arrayListOf("wenig aktiv","leicht aktiv","moderat aktiv","sehr aktiv","extrem aktiv")
    private var contentValue:ArrayList<Float> = arrayListOf(1.2f,1.375f,1.55f,1.725f,1.9f)
    private var argument:String = ""    // Hier noch das Aktivitätenlevel im SharedPrefs vorsehen...
    private var value:Float

    init {

        if(argument != "")
        {
            value = contentValue[content.indexOf(argument)]
        }
        else
        {
            value = 1f
        }
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_autocalc_kcal, null)
        builder.setView(view)

        prefs = SharedAppPreferences(context)


        initViews()



        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }


    private fun initViews()
    {
        // Buttons initialisieren:
        btnSave = view.findViewById(R.id.dialog_autocalc_kcal_btn_save)
        btnAbort = view.findViewById(R.id.dialog_autocalc_kcal_btn_abort)
        btnInfo = view.findViewById(R.id.dialog_autocalc_kcal_btn_info)

        // Button Listener:
        btnSave.setOnClickListener(this)
        btnAbort.setOnClickListener(this)
        btnInfo.setOnClickListener(this)

        // ListView initialisieren:
        listView = view.findViewById(R.id.dialog_autocalc_kcal_listview)
        // ArrayAdapter:
        var adapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_single_choice,content)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        listView.adapter = adapter
        if(argument!="")
        listView.setItemChecked(content.indexOf(argument),true)

        // Listener hinzufügen...
        listView.onItemClickListener = this

    }


    // Implementierte Methoden:
    override fun onClick(v: View?) {
        if(v == btnAbort)
        {
            alertDialog.dismiss()
        }
        else if(v == btnSave)
        {
            // Interface starten und argument übergeben:
            if(mListener!=null)
            {
                if(argument != null && argument!="")
                {
                    mListener.setOnDialogAutoCalcKcalListener(argument,value)
                    alertDialog.dismiss()
                }

            }
        }
        else if(v == btnInfo)
        {
            // InfoDialog öffnen:
            var dialogContent:ArrayList<String> = arrayListOf(
                "wenig aktiv - kein oder wenig Training, überwiegend sitzende Tätigkeit",
                "leicht aktiv - leichtes Training an 1-3 Tagen pro Woche",
                "moderat aktiv - moderates Training an 3-5 Tagen",
                "sehr aktiv - anstregendes Training an 6-7 Tagen",
                "extrem aktiv - anstrengedes tägles Training, harte Körperliche Artbeit"
            )
            var dialog = DialogSingleList("Info zu Aktivitätslevel","Hier erhalten Sie einen Überblick über die verschiedenen Aktivitätslevel",dialogContent,context,false)

        }
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        argument = content[position]
        value = contentValue[position]
    }

    // Interface
    interface OnDialogAutoCalcKcalListener
    {
        fun setOnDialogAutoCalcKcalListener(arg:String, value:Float)
    }

    fun setOnDialogAutoCalcKcalListener(mListener:OnDialogAutoCalcKcalListener)
    {
        this.mListener = mListener
    }
}