package de.rohnert.smeasy.helper.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import de.rohnert.smeasy.R

class DialogSingleMessage(var title:String,var subTitle:String,var message:String,var context:Context)
{
    lateinit var builder: AlertDialog.Builder
    lateinit var helper:backend.helper.Helper
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater

    // View Elemente:
    lateinit var tvTitle: TextView
    lateinit var tvSubTitle: TextView
    lateinit var tvMessage: TextView
    lateinit var btnOk: Button




    init {
        initDialog()
    }

    fun initDialog()
    {
        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_single_message, null)
        builder.setView(view)
        helper = backend.helper.Helper()


        initDialogView()

        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()
    }

    fun initDialogView()
    {
        // Init Views...
        tvTitle = view.findViewById(R.id.dialog_singlemessage_title)
        tvSubTitle = view.findViewById(R.id.dialog_singlemessage_subtitle)

        tvMessage = view.findViewById(R.id.dialog_singlemessage_message)

        // Buttons:
        btnOk = view.findViewById(R.id.dialog_singlemessage_btn_ok)


        // Init Content:
        tvTitle.text = title
        tvSubTitle.text = subTitle
        tvMessage.text = message

        btnOk.setOnClickListener(View.OnClickListener {
            alertDialog.dismiss()
        })
    }
}