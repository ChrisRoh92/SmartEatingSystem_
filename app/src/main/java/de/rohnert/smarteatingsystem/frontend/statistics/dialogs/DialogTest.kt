package de.rohnert.smarteatingsystem.frontend.statistics.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import de.rohnert.smarteatingsystem.R

class DialogTest(var context:Context)
{

    //
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater


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





        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }
}