package de.rohnert.smeasy.frontend.premium.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import de.rohnert.smeasy.R

class DialogPremiumAlert(var context: Context)
{

    lateinit var builder: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater

    // Views
    lateinit var btnAbort:Button
    lateinit var btnShare:Button

    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_premium_alert, null)
        builder.setView(view)


        initButtons()




        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initButtons()
    {
        btnAbort = view.findViewById(R.id.dialog_premium_btn_abort)
        btnShare = view.findViewById(R.id.dialog_premium_btn_share)

        btnAbort.setOnClickListener {
            alertDialog.dismiss()
        }

        btnShare.setOnClickListener {
            Toast.makeText(context,"Vielen Dank, dass du für Smeasy geworben hast",Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }
    }
}