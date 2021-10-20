package de.rohnert.smarteatingsystem.ui.foodtracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import de.rohnert.smarteatingsystem.data.helper.Helper
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smarteatingsystem.R

class DialogExtendFilter(var context: Context,var values:ArrayList<Float>) : View.OnClickListener {


    // Allgemeine Variablen:
    private var helper = Helper()

    // Dialog Stuff
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater

    // Interface:
    private lateinit var mListener:OnDialogExtendedFilterListener

    // View Elemente:
    // Buttons:
    private lateinit var btnSave: Button
    private lateinit var btnAbort:Button

    // TextInputLayouts...
    private lateinit var etKcalMin:TextInputLayout
    private lateinit var etKcalMax:TextInputLayout
    private lateinit var etCarbMin:TextInputLayout
    private lateinit var etCarbMax:TextInputLayout
    private lateinit var etProteinMin:TextInputLayout
    private lateinit var etProteinMax:TextInputLayout
    private lateinit var etFettMin:TextInputLayout
    private lateinit var etFettMax:TextInputLayout



    init {
        initDialog()
    }

    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_extend_filter, null)
        builder.setView(view)


        initViews()
        //initRecyclerView()
        //initChipGroupContent()



        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initViews()
    {

        // Buttons initialisieren:
        btnSave = view.findViewById(R.id.dialog_extend_filter_btn_save)
        btnAbort = view.findViewById(R.id.dialog_extend_filter_btn_abort)

        // Btn Listener initialisieren:
        btnSave.setOnClickListener(this)
        btnAbort.setOnClickListener(this)

        // TextInputLayouts initialisieren:
        etKcalMin = view.findViewById(R.id.dialog_extend_filter_et_kcal_min)
        etKcalMax = view.findViewById(R.id.dialog_extend_filter_et_kcal_max)

        etCarbMin = view.findViewById(R.id.dialog_extend_filter_et_carb_min)
        etCarbMax = view.findViewById(R.id.dialog_extend_filter_et_carb_max)

        etProteinMin = view.findViewById(R.id.dialog_extend_filter_et_protein_min)
        etProteinMax = view.findViewById(R.id.dialog_extend_filter_et_protein_max)

        etFettMin = view.findViewById(R.id.dialog_extend_filter_et_fett_min)
        etFettMax = view.findViewById(R.id.dialog_extend_filter_et_fett_max)

        var etList:ArrayList<TextInputLayout> =
            arrayListOf(
                etKcalMin,etKcalMax,
                etCarbMin,etCarbMax,
                etProteinMin,etProteinMax,
                etFettMin,etFettMax)
        // Text setzen:
        for((index,i) in etList.withIndex())
        {
            if(values[index] != 0f)
            {
                i.editText!!.setText(helper.getFloatAsFormattedString(values[index]))
            }
        }

    }

    private fun getInputDatas():ArrayList<Float>
    {
        var etList:ArrayList<TextInputLayout> =
            arrayListOf(
                etKcalMin,etKcalMax,
                etCarbMin,etCarbMax,
                etProteinMin,etProteinMax,
                etFettMin,etFettMax)

        var export:ArrayList<Float> = ArrayList()
        for(i in etList)
        {
            if(i.editText!!.text.isNullOrEmpty())
            {
                export.add(0f)
            }
            else
            {
                export.add(i.editText!!.text.toString().toFloat())
            }
        }

        return export
    }

    // Implementierte Methoden:
    override fun onClick(v: View?) {
        if(v == btnAbort)
        {
            alertDialog.dismiss()
        }
        else
        {
            if(mListener != null)
            {
                mListener.setOnDialogExtendedFilterListener(getInputDatas())
                alertDialog.dismiss()
            }
        }
    }

    // Interface
    interface OnDialogExtendedFilterListener
    {
        /*fun setOnDialogExtendedFilterListener(minKcal:Double,maxKcal:Double,
                                              minCarb:Double,maxCarb:Double,
                                              minProtein:Double,maxProtein:Double,
                                              minFett:Double,maxFett:Double)*/

        fun setOnDialogExtendedFilterListener(values:ArrayList<Float>)

    }

    fun setOnDialogExtendedFilterListener(mListener:OnDialogExtendedFilterListener)
    {
        this.mListener = mListener
    }
}