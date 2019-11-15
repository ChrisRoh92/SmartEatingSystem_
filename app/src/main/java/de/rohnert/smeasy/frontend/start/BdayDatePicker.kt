package de.rohnert.smeasy.frontend.start

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import de.rohnert.smeasy.R
import java.util.*

class BdayDatePicker(var context: Context) : DatePickerDialog.OnDateSetListener {

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var calendar = Calendar.getInstance()
        calendar.set(year,month,dayOfMonth)
        mListener.setOnDialogClickListener(calendar.time)
    }


    private lateinit var mListener: OnDialogClickListener

    init {
        startCalendarDialog()
    }


    private fun startCalendarDialog()
    {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(context,this,year,month,day)
        //dialog.datePicker.spinnersShown = true


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