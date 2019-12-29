package de.rohnert.smarteatingsystem.frontend.premium.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import de.rohnert.smarteatingsystem.R

class DialogPremiumAlert(var context: Context)
{

    lateinit var builder: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater

    // Views
    lateinit var btnShareOne:ImageButton
    lateinit var btnShareTwo:Button

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
        btnShareOne = view.findViewById(R.id.dialog_premium_btn_fb_image)
        btnShareTwo = view.findViewById(R.id.dialog_premium_btn_fb)

        btnShareOne.setOnClickListener {
            Toast.makeText(context,"Vielen Dank, dass du für SmartEatingSystem geworben hast",Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        btnShareOne.setOnClickListener {
            Toast.makeText(context,"Vielen Dank, dass du für SmartEatingSystem geworben hast",Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }
    }
}