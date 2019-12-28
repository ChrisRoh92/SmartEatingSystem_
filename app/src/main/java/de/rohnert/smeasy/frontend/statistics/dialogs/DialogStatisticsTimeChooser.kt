package de.rohnert.smeasy.frontend.statistics.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import de.rohnert.smeasy.backend.helper.Helper
import de.rohnert.smeasy.R

class DialogStatisticsTimeChooser(var context: Context,var timeValue:String) : View.OnClickListener,
    AdapterView.OnItemClickListener {



    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    // Interface:
    private lateinit var mListener:OnDialogSaveClickListener
    // Views:
    private lateinit var list: ListView
    private lateinit var adapter:ArrayAdapter<String>
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button
    // Content
    var values:ArrayList<String> = arrayListOf("Letzte 7 Tage","Letzte 14 Tage","Letzte 30 Tage","Letzte 60 Tage","Letzte 90 Tage")
    var value = timeValue



    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_statistic_time_chooser, null)
        builder.setView(view)

        initViews()

        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initViews()
    {
        // InitButtons:
        btnSave = view.findViewById(R.id.dialog_statistic_time_chooser_btn_save)
        btnAbort = view.findViewById(R.id.dialog_statistic_time_chooser_btn_abort)

        // Listener für die Buttons:
        btnSave.setOnClickListener(this)
        btnAbort.setOnClickListener(this)

        // ListView:
        list = view.findViewById(R.id.dialog_statistic_time_chooser_listview)
        adapter = ArrayAdapter(context,android.R.layout.simple_list_item_single_choice,values)
        list.choiceMode = ListView.CHOICE_MODE_SINGLE

        list.adapter = adapter
        list.setItemChecked(values.indexOf(value),true)

        // Listener hinzufügen...
        list.onItemClickListener = this


    }

    // Implementierte Methoden
    override fun onClick(v: View?) {
        if(v == btnSave)
        {
           if(mListener != null)
           {
               mListener.setOnDialogSaveClickListener(value)
               alertDialog.dismiss()
           }
        }
        else
        {
            alertDialog.dismiss()
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        value = values[position]
    }


    // Interface:
    interface OnDialogSaveClickListener
    {
        fun setOnDialogSaveClickListener(value:String)
    }

    fun setOnDialogSaveClickListener( mListener:OnDialogSaveClickListener)
    {
        this.mListener = mListener
    }
}