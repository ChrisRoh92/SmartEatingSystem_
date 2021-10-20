package de.rohnert.smarteatingsystem.ui.bodytracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import de.rohnert.smarteatingsystem.data.helper.Helper
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.utils.dialogs.DialogSingleChoiceList

class DialogBodySettingsAim(var context: Context,var prefs:SharedAppPreferences ) :
    AdapterView.OnItemClickListener, View.OnClickListener {



    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private var helper = Helper()

    // Content:
    private lateinit var content:ArrayList<String>
    private var aim = prefs.aim
    private var aimWeightLoss = prefs.aimWeightLoss

    // Views:
    private lateinit var listView:ListView
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button
    private lateinit var btnWochenZiel:ImageButton
    private lateinit var tvWochenZiel:TextView

    // Interface:
    private lateinit var mListener:OnDialogBodySettingsAimListener


    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_bodysettings_aim, null)
        builder.setView(view)


        initViews()


        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initViews()
    {
        // ListView
        listView = view.findViewById(R.id.dialog_bodysettings_aim_listview)
        content= arrayListOf("Abnehmen","Gewicht Halten","Aufbauen")
        // ArrayAdapter:
        var adapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_single_choice,content)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        listView.adapter = adapter
        listView.setItemChecked(content.indexOf(aim),true)

        // Listener hinzufügen...
        listView.onItemClickListener = this

        tvWochenZiel = view.findViewById(R.id.dialog_bodysettings_aim_tv_wochenziel)
        tvWochenZiel.text = "$aimWeightLoss kg pro Woche"



        // EditText:
        /*et = view.findViewById(R.id.dialog_bodysettings_aim_et)
        et.editText!!.setText(helper.getFloatAsFormattedStringWithPattern(aimWeightLoss,"#.##"))*/


        // Buttons:
        btnSave = view.findViewById(R.id.dialog_bodysettings_aim_btn_save)
        btnAbort = view.findViewById(R.id.dialog_bodysettings_aim_btn_abort)
        btnWochenZiel = view.findViewById(R.id.dialog_bodysettings_aim_btn_wochenziel)


        btnAbort.setOnClickListener{alertDialog.dismiss()}

        var items:ArrayList<Float> = arrayListOf(-1f,-0.75f,-0.5f,-0.25f,0f,0.25f,0.5f,0.75f,1f)
        var contentList:ArrayList<String> = ArrayList()
        for(i in items) contentList.add("${helper.getFloatAsFormattedStringWithPattern(i,"#.##")} kg pro Woche")
        btnWochenZiel.setOnClickListener {
            var pos = items.indexOf(aimWeightLoss)
            var dialog = DialogSingleChoiceList("Wochenziel auswählen","Wähle aus der Liste dein Wochenziel aus...",contentList,view.context,true,pos)
            dialog.onItemClickListener(object:DialogSingleChoiceList.OnDialogListListener{
                override fun onItemClickListener(value: String, pos: Int)
                {
                    aimWeightLoss = items[pos]
                    tvWochenZiel.text = "$aimWeightLoss kg pro Woche"
                    // Speichern in prefs:
                    prefs.setNewAimWeightLoss(aimWeightLoss)



                }

            })
        }

        btnSave.setOnClickListener(this)




        /*btnSave.setOnClickListener{
            if(et.editText!!.text.isNullOrEmpty())
            {
                aimWeightLoss = 0f
                et.editText!!.setText(helper.getFloatAsFormattedStringWithPattern(aimWeightLoss,"#.##"))
            }
            else
            {
                // Do Nothing...
            }
            if(mListener!=null)
            {
                try {
                    if(et.editText!!.text.toString().contains(','))
                    {
                        var newString:String = et.editText!!.text.toString().replace(",",".")
                        et.editText!!.setText(newString)
                        aimWeightLoss = et.editText!!.text.toString().toFloat()
                    }
                    aimWeightLoss = et.editText!!.text.toString().toFloat()
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
                if(aim == "Gewicht Halten" && aimWeightLoss != 0f)
                {
                    Toast.makeText(context,"Beim Ziel 'Gewicht Halten' muss dein Wochenziel 0 kg pro Woche betragen",Toast.LENGTH_SHORT).show()
                }
                else if(aim == "Abnehmen" && aimWeightLoss > 0f)
                {
                    Toast.makeText(context,"Beim Ziel 'Abnehmen' muss dein Wochenziel kleiner 0 kg pro Woche betragen",Toast.LENGTH_SHORT).show()
                }

                else if(aim == "Aufbauen" && aimWeightLoss < 0f)
                {
                    Toast.makeText(context,"Beim Ziel 'Aufbauen' muss dein Wochenziel größer 0 kg pro Woche betragen",Toast.LENGTH_SHORT).show()
                }
                else if(kotlin.math.abs(aimWeightLoss) <= 2)
                {

                    mListener.setOnDialogBodySettingsAimListener(aim,aimWeightLoss)
                    alertDialog.dismiss()
                }
                else
                {
                  Toast.makeText(context,"Das Wochenziel soll nicht mehr wie +2 bzw. -2 kg pro Woche betragen",Toast.LENGTH_SHORT).show()
                }

            }
        }*/
    }





    // Interface:
    interface OnDialogBodySettingsAimListener
    {
        fun setOnDialogBodySettingsAimListener(aim:String,value:Float)
    }

    fun setOnDialogBodySettingsAimListener(mListener:OnDialogBodySettingsAimListener)
    {
        this.mListener = mListener
    }



    // Implementierte Methoden:

    override fun onClick(v: View?) {
        if(v == btnSave)
        {

            if(aim == "Gewicht Halten" && aimWeightLoss != 0f)
            {
                Toast.makeText(context,"Beim Ziel 'Gewicht Halten' muss dein Wochenziel 0 kg pro Woche betragen",Toast.LENGTH_SHORT).show()
            }
            else if(aim == "Abnehmen" && aimWeightLoss > 0f)
            {
                Toast.makeText(context,"Beim Ziel 'Abnehmen' muss dein Wochenziel kleiner 0 kg pro Woche betragen",Toast.LENGTH_SHORT).show()
            }

            else if(aim == "Aufbauen" && aimWeightLoss < 0f)
            {
                Toast.makeText(context,"Beim Ziel 'Aufbauen' muss dein Wochenziel größer 0 kg pro Woche betragen",Toast.LENGTH_SHORT).show()
            }
            else
            {
                mListener.setOnDialogBodySettingsAimListener(aim,aimWeightLoss)
                alertDialog.dismiss()
            }

        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        aim = content[position]
    }
}