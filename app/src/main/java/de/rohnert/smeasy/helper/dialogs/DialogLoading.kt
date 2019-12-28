package de.rohnert.smeasy.helper.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.helper.Helper

class DialogLoading(var context: Context)
{
    lateinit var builder: AlertDialog.Builder
    lateinit var helper: Helper
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater

    init {
        initDialog()
    }

    fun initDialog()
    {
        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_loading, null)
        builder.setView(view)
        helper = Helper()
        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()
    }

    fun dismiss()
    {
        alertDialog.dismiss()
    }
}