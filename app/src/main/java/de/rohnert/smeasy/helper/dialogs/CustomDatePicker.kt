package de.rohnert.smeasy.helper.dialogs

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import java.util.*

class CustomDatePicker(): DialogFragment() {


    // Allgemeine Variablen:
    private lateinit var rootView: View

    // Content:
    private lateinit var exportCalendar: Calendar

    // Interface
    private lateinit var mListener:OnCustomDatePickerListener

    // Views:
    private lateinit var datePicker:DatePicker
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        Log.d("Smeasy", "DialogFragmentFoodList - onCreateView")
        rootView = inflater.inflate(R.layout.dialog_customdatepicker, container, false)


        initViews()



        return rootView
    }

    private fun initViews()
    {
        datePicker = rootView.findViewById(R.id.dialog_customdatepicker_datepicker)
        val calendar = Calendar.getInstance()
        val sYear = calendar.get(Calendar.YEAR)
        val sMonth = calendar.get(Calendar.MONTH)
        val sDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        exportCalendar = Calendar.getInstance()
        datePicker.init(sYear,sMonth,sDayOfMonth
        ) { _, year, month, dayOfMonth ->
            exportCalendar.set(year,month,dayOfMonth)
        }

        btnSave = rootView.findViewById(R.id.dialog_customdatepicker_btn_save)
        btnAbort = rootView.findViewById(R.id.dialog_customdatepicker_btn_abort)

        btnAbort.setOnClickListener { this.dismiss() }

        btnSave.setOnClickListener {
            if(mListener!=null)
            {
                if(exportCalendar!=null)
                {
                    mListener.setOnCustomDatePickerListener(exportCalendar.time)
                    dismiss()
                }
                else
                {
                    Toast.makeText(rootView.context,"Bitte ein Datum wählen",Toast.LENGTH_SHORT).show()

                }

            }
        }


        //

    }


    // Interface:
    interface OnCustomDatePickerListener
    {
        fun setOnCustomDatePickerListener(date:Date)
    }

    fun setOnCustomDatePickerListener(mListener:OnCustomDatePickerListener)
    {
        this.mListener = mListener
    }





}