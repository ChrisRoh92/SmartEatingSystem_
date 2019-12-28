package de.rohnert.smeasy.frontend.helper.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import de.rohnert.smeasy.backend.helper.Helper
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences

class DialogLicenses(var context: Context)
{
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private lateinit var share: SharedAppPreferences

    // View Elemente:
    private lateinit var btn:Button

    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_helper_licenses, null)
        builder.setView(view)

        share = SharedAppPreferences(context)

        initButton()




        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initButton()
    {
        btn = view.findViewById(R.id.dialog_helper_licenses_btn)
        btn.setOnClickListener { alertDialog.dismiss() }
    }
}