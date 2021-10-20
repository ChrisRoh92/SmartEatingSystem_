package de.rohnert.smarteatingsystem.ui.foodtracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import de.rohnert.smarteatingsystem.R

class DialogMealEntry(var context: Context)
{
    lateinit var builder: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater

    // Views:
    lateinit var btnVerschieben: Button
    private lateinit var btnDelete: Button

    // Interface:
    private lateinit var mListener:OnDialogMealEntryClickListener

    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_mealentry_longclick, null)
        builder.setView(view)

        initViews()

        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initViews()
    {
        btnVerschieben = view.findViewById(R.id.dialog_mealentry_btn_verschieben)
        btnDelete = view.findViewById(R.id.dialog_mealentry_btn_delete)

        btnVerschieben.setOnClickListener {
            saveProcess(false)

        }

        btnDelete.setOnClickListener{
            saveProcess(true)
        }
    }

    private fun saveProcess(delete:Boolean)
    {
        if(mListener!=null)
        {
            mListener.setOnDialogClickListener(delete)
            alertDialog.dismiss()
        }
    }


    interface OnDialogMealEntryClickListener
    {
        fun setOnDialogClickListener(delete:Boolean)

    }

    fun setOnDialogClickListener(mListener:OnDialogMealEntryClickListener)
    {
        this.mListener = mListener
    }

}