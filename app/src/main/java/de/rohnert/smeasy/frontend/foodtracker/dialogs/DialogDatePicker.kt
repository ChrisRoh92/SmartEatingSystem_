package de.rohnert.smeasy.frontend.foodtracker.dialogs

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import java.util.*


class DialogDatePicker(var context: Context, var view: View)
{


    private lateinit var mListener: OnDialogClickListener

    init {
        startCalendarDialog()
    }


    private fun startCalendarDialog()
    {
        val dialog = DatePickerDialog(context)
        dialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            var calendar = Calendar.getInstance()
            calendar.set(year,month,dayOfMonth)
            mListener.setOnDialogClickListener(calendar.time)
        }
        dialog.show()
    }


    // Interface:
    interface OnDialogClickListener
    {
        fun setOnDialogClickListener(date:Date)
    }

    fun setOnDialogClickListener(mListener:OnDialogClickListener)
    {
        this.mListener = mListener
    }
}