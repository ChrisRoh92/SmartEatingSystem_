package de.rohnert.smarteatingsystem.utils.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import android.content.Intent
import android.net.Uri
import de.rohnert.smarteatingsystem.backend.helper.Helper


class DialogAppRating(var context: Context) : View.OnClickListener {


    private lateinit var builder: AlertDialog.Builder
    private lateinit var helper: Helper
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater

    // Einstellungen:
    private lateinit var prefs:SharedAppPreferences

    // View Elemente:
    private lateinit var btnStart:Button
    private lateinit var btnLater:Button
    private lateinit var btnNever:Button


    init {
        initDialog()
    }

    fun initDialog()
    {
        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_app_rating, null)
        builder.setView(view)


        helper = Helper()
        prefs = SharedAppPreferences(context)

        initButtons()



        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }

    private fun initButtons()
    {
        btnStart = view.findViewById(R.id.dialog_apprating_btn_start)
        btnLater = view.findViewById(R.id.dialog_apprating_btn_later)
        btnNever = view.findViewById(R.id.dialog_apprating_btn_never)

        // Listener:
        btnStart.setOnClickListener(this)
        btnLater.setOnClickListener(this)
        btnNever.setOnClickListener(this)


    }

    // Implementierte Methoden:
    override fun onClick(v: View?) {
        if(v == btnStart)
        {
            context.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=" + context.packageName)))
            prefs.setNewRateStatus(true)
            prefs.setNewRateCountAppStart(0)
            alertDialog.dismiss()
        }
        else if(v == btnLater)
        {
            prefs.setNewRateCountAppStart(0)
            alertDialog.dismiss()
        }
        else if(v == btnNever)
        {
            prefs.setNewRateNeverStatus(true)
            alertDialog.dismiss()
        }
    }


}