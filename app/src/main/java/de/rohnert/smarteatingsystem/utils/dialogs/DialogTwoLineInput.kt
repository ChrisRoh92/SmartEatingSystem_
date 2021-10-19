package de.rohnert.smarteatingsystem.utils.dialogs

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.helper.Helper

class DialogTwoLineInput(var title:String, var subTitle:String, var context: Context,
                         var inputType1: Int,var inputType2: Int,
                         var value1:String = "",var value2:String = "")
{
    lateinit var builder: AlertDialog.Builder
    lateinit var helper: Helper
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater

    // View Elemente:
    lateinit var tvTitle: TextView
    lateinit var tvSubTitle: TextView
    lateinit var et1: EditText
    lateinit var et2: EditText
    lateinit var btnSave: Button
    lateinit var btnAbort: Button

    // Stuff for Interface:
    lateinit var mListener:OnDialogListener

    init {
        initDialog()
    }


    fun initDialog()
    {
        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_double_line_input, null)
        builder.setView(view)
        helper = Helper()


        initDialogView()

        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()
    }

    private fun initDialogView()
    {
        // Init Views...
        tvTitle = view.findViewById(R.id.dialog_doubleline_title)
        tvSubTitle = view.findViewById(R.id.dialog_doubleline_subtitle)

        et1 = view.findViewById(R.id.dialog_doubleline_et1)
        et2 = view.findViewById(R.id.dialog_doubleline_et2)

        // Buttons:
        btnSave = view.findViewById(R.id.dialog_doubleline_btn_save)
        btnAbort = view.findViewById(R.id.dialog_doubleline_btn_abort)

        // Init Content:
        tvTitle.text = title
        tvSubTitle.text = subTitle

        // InputType1:
        if((inputType1 == InputType.TYPE_CLASS_NUMBER) or (inputType1 == InputType.TYPE_NUMBER_FLAG_DECIMAL))
        {
            et1.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        }
        else
        {
            et1.inputType = (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL)
        }

        // InputType2:
        if((inputType2 == InputType.TYPE_CLASS_NUMBER) or (inputType2 == InputType.TYPE_NUMBER_FLAG_DECIMAL))
        {
            et2.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        }
        else
        {
            et2.inputType = (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL)
        }

        et1.setText(value1)
        et2.setText(value2)

        btnAbort.setOnClickListener {
            alertDialog.dismiss()
        }

        // EingabeWert übergeben...
        btnSave.setOnClickListener {

            if (mListener != null) {
                if (et1.text.isNotEmpty() && et2.text.isNotEmpty()) {

                    mListener.setOnDialogClickListener(et1.text.toString(),et2.text.toString())

                }
                alertDialog.dismiss()
            }
        }


    }










    // Interface
    interface OnDialogListener
    {
        fun setOnDialogClickListener(export1:String,export2:String)

    }

    fun setOnDialogClickListener(mListener:OnDialogListener)
    {
        this.mListener = mListener
    }
}