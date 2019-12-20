package de.rohnert.smeasy.helper.dialogs

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import de.rohnert.smeasy.R

class DialogSingleLineInput(var title:String,var subTitle:String, var context: Context, var inputType: Int, var value:String = "")
{

    lateinit var builder: AlertDialog.Builder
    lateinit var helper:backend.helper.Helper
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater

    // View Elemente:
    lateinit var tvTitle:TextView
    lateinit var tvSubTitle:TextView
    lateinit var et:EditText
    lateinit var btnSave: Button
    lateinit var btnAbort:Button

    // Stuff for Interface:
    lateinit var mListener:OnDialogListener

    init {
        initDialog()
    }


    fun initDialog()
    {
        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_single_line_input, null)
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
        tvTitle = view.findViewById(R.id.dialog_singleline_title)
        tvSubTitle = view.findViewById(R.id.dialog_singleline_subtitle)

        et = view.findViewById(R.id.dialog_singleline_et)

        // Buttons:
        btnSave = view.findViewById(R.id.dialog_singleline_btn_save)
        btnAbort = view.findViewById(R.id.dialog_singleline_btn_abort)

        // Init Content:
        tvTitle.text = title
        tvSubTitle.text = subTitle

        // InputType:
        if((inputType == InputType.TYPE_CLASS_NUMBER) or (inputType == InputType.TYPE_NUMBER_FLAG_DECIMAL))
        {
            et.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        }
        else
        {
            et.inputType = (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL)
        }

        et.setText(value)

        btnAbort.setOnClickListener(View.OnClickListener {
            alertDialog.dismiss()
        })

        // EingabeWert übergeben...
        btnSave.setOnClickListener(View.OnClickListener {
            if(mListener!=null)
            {
                if(!et.text.isEmpty())
                {
                    if(et.text.matches("-?\\d+(\\.\\d+)?".toRegex()))
                    {
                        mListener.onDialogClickListener(et.text.toString().toFloat())
                    }
                    else
                    {
                        mListener.onDialogClickListener(et.text.toString())
                    }

                }
                alertDialog.dismiss()
            }
        })






    }










    // Interface
    interface OnDialogListener
    {
        fun onDialogClickListener(export:String)
        fun onDialogClickListener(export:Float)
    }

    fun onDialogClickListener(mListener:OnDialogListener)
    {
        this.mListener = mListener
    }

}