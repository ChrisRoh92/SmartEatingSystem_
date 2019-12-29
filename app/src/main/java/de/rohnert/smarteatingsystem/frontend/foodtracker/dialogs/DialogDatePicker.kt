package de.rohnert.smarteatingsystem.frontend.foodtracker.dialogs

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import java.util.*


class DialogDatePicker(var context: Context, var view: View) : DatePickerDialog.OnDateSetListener {



    private lateinit var mListener: OnDialogClickListener

    init {
        startCalendarDialog()
    }


    private fun startCalendarDialog()
    {
        var today = Date()
        var iCal = Calendar.getInstance()
        iCal.time = today
        var year = iCal.get(Calendar.YEAR)
        var month = iCal.get(Calendar.MONTH)
        var day = iCal.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(context,this,year,month,day)
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

    // Implementierte Methoden:
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var calendar = Calendar.getInstance()
        calendar.set(year,month,dayOfMonth)
        mListener.setOnDialogClickListener(calendar.time)
    }
}