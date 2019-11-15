package de.rohnert.smeasy.helper.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import de.rohnert.smeasy.R

class DialogSingleList(var title:String,var subTitle:String, var content:ArrayList<String>,var context: Context) :
    AdapterView.OnItemClickListener {


    lateinit var builder: AlertDialog.Builder
    lateinit var helper:backend.helper.Helper
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater

    // View Elemente:
    lateinit var tvTitle: TextView
    lateinit var tvSubTitle: TextView
    lateinit var list:ListView

    // Interface:
    lateinit var mListener:OnDialogListListener


    init {
        initDialog()
    }

    fun initDialog()
    {
        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_single_list, null)
        builder.setView(view)
        helper = backend.helper.Helper()


        initDialogView()

        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()
    }

    fun initDialogView()
    {
        tvTitle = view.findViewById(R.id.dialog_singlelist_title)
        tvSubTitle = view.findViewById(R.id.dialog_singlelist_subtitle)
        list = view.findViewById(R.id.dialog_singlelist_listview)

        // Set Text
        tvTitle.text = title
        tvSubTitle.text = subTitle

        // ArrayAdapter:
        var adapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,content)
        list.adapter = adapter

        // Listener hinzufügen...
        list.onItemClickListener = this

    }


    override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if(mListener!=null)
        {
            mListener.onItemClickListener(content[position],position)
            alertDialog.dismiss()
        }
    }


    // Interface
    interface OnDialogListListener
    {
        fun onItemClickListener(value:String,pos:Int)
    }

    fun onItemClickListener(mListener:OnDialogListListener)
    {
        this.mListener = mListener
    }
}