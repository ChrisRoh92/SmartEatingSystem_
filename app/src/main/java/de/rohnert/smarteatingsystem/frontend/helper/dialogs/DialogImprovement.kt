package de.rohnert.smarteatingsystem.frontend.helper.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences

class DialogImprovement(var context: Context)
{
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private lateinit var share: SharedAppPreferences

    // View Elemente:
    private lateinit var btn: Button
    private lateinit var btnEmail: Button

    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_helper_improvement, null)
        builder.setView(view)

        share = SharedAppPreferences(context)


        initButtons()


        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initButtons()
    {
        btn = view.findViewById(R.id.dialog_helper_improvement_btn)
        btnEmail = view.findViewById(R.id.dialog_helper_improvement_btn_mail)

        // Listener:

        btn.setOnClickListener { alertDialog.dismiss() }

        btnEmail.setOnClickListener {
            sendEmail()
            alertDialog.dismiss()
        }
    }

    private fun sendEmail()
    {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = ("message/rfc822")
        intent.data = Uri.parse("mailto:Christoph.Rohnert@t-online.de")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Verbesserungsvorschlag für SmartEatingSystem")

        var androidVersion = Build.VERSION.SDK_INT
        var manufactur = Build.MANUFACTURER
        var model = Build.MODEL
        var versionRelease = Build.VERSION.RELEASE
        var content = "Bitte Trage hier dein Verbesserungsvorschlag ein \n\n" +
                "Deine Android-Version: $androidVersion \n" +
                "Hersteller von deinem SmartPhone: $manufactur \n" +
                "Smartphone Modell: $model \n" +
                "Deine ReleaseVersion: $versionRelease + \n"
        intent.putExtra(Intent.EXTRA_TEXT, content)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


        context.startActivity(Intent.createChooser(intent, "Email an SmartEatingSystem senden..."))
    }
}