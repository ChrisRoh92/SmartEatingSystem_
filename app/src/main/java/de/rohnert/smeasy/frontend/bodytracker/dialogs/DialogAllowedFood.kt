package de.rohnert.smeasy.frontend.bodytracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences

class DialogAllowedFood(var context: Context,var min:Float,var max:Float,var unit:String,var pos:Int) :
    View.OnClickListener {


    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private lateinit var prefs: SharedAppPreferences

    // Interface:
    private lateinit var mListener:OnDialogButtonClickListener

    // View Elemente:
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button
    private lateinit var etMin:TextInputLayout
    private lateinit var etMax:TextInputLayout

    private var hintMinContent:ArrayList<String> = arrayListOf("Kalorien min.","Kohlenhydrate min.","Protein min.","Fett min.")
    private var hintMaxContent:ArrayList<String> = arrayListOf("Kalorien max.","Kohlenhydrate max.","Protein max.","Fett max.")






    init {
        initDialog()
    }

    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_allowed_food, null)
        builder.setView(view)

        prefs = SharedAppPreferences(context)


        initViews()



        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initViews()
    {
        // Buttons initialisieren:
        btnSave = view.findViewById(R.id.dialog_allowedfood_btn_save)
        btnAbort = view.findViewById(R.id.dialog_allowedfood_btn_abort)

        // Listener installieren:
        btnSave.setOnClickListener(this)
        btnAbort.setOnClickListener { alertDialog.dismiss() }

        // Ets initialisieren:
        etMin = view.findViewById(R.id.dialog_allowedfood_et_min)
        etMax = view.findViewById(R.id.dialog_allowedfood_et_max)

        etMin.hint = hintMinContent[pos]
        etMax.hint = hintMaxContent[pos]

        if(min <= 0f)
        {
            etMin.editText!!.setText("")
        }
        else
        {
            etMin.editText!!.setText(min.toString())
        }

        if(max <= 0f)
        {
            etMax.editText!!.setText("")
        }
        else
        {
            etMax.editText!!.setText(max.toString())

        }


    }



    interface OnDialogButtonClickListener
    {
        fun setOnDialogButtonClickListener(min:Float,max:Float)
    }

    fun setOnDialogButtonClickListener(mListener:OnDialogButtonClickListener)
    {
        this.mListener = mListener
    }

    // Implementierte Methoden:
    override fun onClick(v: View?)
    {
        if(v == btnSave)
        {
            var min = getDataFromEt()[0]
            var max = getDataFromEt()[1]
            if(max == 0f)
            {
                max = -1f
            }
            if(min > max && max != -1f)
            {
                etMin.editText!!.setText(max.toString())
                min = max
            }
            if(mListener != null)
            {
                mListener.setOnDialogButtonClickListener(min,max)
                alertDialog.dismiss()
            }
        }





    }

    private fun getDataFromEt():ArrayList<Float>
    {
        var min = 0f
        var max = 0f
        try {
            if(etMin.editText!!.text.toString().contains(','))
            {
                var newString:String = etMin.editText!!.text.toString().replace(",",".")
                etMin.editText!!.setText(newString)
            }
            if(etMin.editText!!.text.isNullOrEmpty())
            {
                min = 0f
            }
            else
            {
                min = etMin.editText!!.text.toString().toFloat()
            }




        }catch (e:Exception)
        {
            e.printStackTrace()
        }

        try {
            if(etMax.editText!!.text.toString().contains(','))
            {
                var newString:String = etMax.editText!!.text.toString().replace(",",".")
                etMax.editText!!.setText(newString)
            }
            if(etMax.editText!!.text.isNullOrEmpty())
            {
                max = -1f
            }
            else
            {
                max = etMax.editText!!.text.toString().toFloat()

            }
        }catch (e:Exception)
        {
            e.printStackTrace()
        }

        return arrayListOf(min,max)

    }
}