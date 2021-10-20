package de.rohnert.smarteatingsystem.ui.foodtracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import de.rohnert.smarteatingsystem.R
import java.util.*

class DialogMealList(var context: Context) : AdapterView.OnItemClickListener {


    lateinit var builder: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater

    // Views:
    lateinit var list: ListView
    var export: ArrayList<String> = arrayListOf("breakfast","lunch","dinner","snack")

    // Interface:
    private lateinit var mListener: OnDialogMealListClick

    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_meallist_choose, null)
        builder.setView(view)

        initViews()

        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initViews()
    {
        list = view.findViewById(R.id.dialog_meallist_list)
        var content:ArrayList<String> = arrayListOf("Frühstück","Mittag","Abendessen","Snacks")
        var adapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,content)
        list.adapter = adapter
        list.onItemClickListener = this
    }

    // Interface
    interface OnDialogMealListClick
    {
        fun setOnDialogMealListClickListener(value:String)
    }

    fun setOnDialogMealListClickListener(mListener:OnDialogMealListClick)
    {
        this.mListener = mListener
    }

    // Implementierte Funktionen:
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if(mListener!=null)
        {
            mListener.setOnDialogMealListClickListener(export[p2])
            alertDialog.dismiss()
        }
    }
}