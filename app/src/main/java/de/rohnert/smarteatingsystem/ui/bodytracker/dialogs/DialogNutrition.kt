package de.rohnert.smarteatingsystem.ui.bodytracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import de.rohnert.smarteatingsystem.data.helper.Helper
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.ui.bodytracker.BodyViewModel
import kotlin.math.roundToInt

class DialogNutrition(var context: Context, var bodyViewModel:BodyViewModel) : SeekBar.OnSeekBarChangeListener,
    View.OnClickListener {



    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private lateinit var share:SharedAppPreferences
    private var helper = Helper()

    // Interface:
    private lateinit var mListener:OnDialogNutritionListener

    // Content:
    private var maxKcal = 2000
    private var progressCarb = 25
    private var progressProtein = 50
    private var progressFett = 25

    // View Elemente:
    //Buttons:
    private lateinit var btnAuto:Button
    private lateinit var btnAbort:Button
    private lateinit var btnSave:Button
    // TextViews:
    private lateinit var tvCarbs:TextView
    private lateinit var tvProtein:TextView
    private lateinit var tvFett:TextView
    private lateinit var tvCarbsPercent:TextView
    private lateinit var tvProteinPercent:TextView
    private lateinit var tvFettPercent:TextView
    // SeekBar
    private lateinit var skCarb:SeekBar
    private lateinit var skProtein:SeekBar
    private lateinit var skFett:SeekBar
    // EditTexts
    private lateinit var etKcal:TextInputLayout




    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_bodysettings_nutrition, null)
        builder.setView(view)

        share = SharedAppPreferences(context)
        maxKcal = share.maxKcal.roundToInt()
        progressCarb = share.maxCarb.roundToInt()
        progressProtein = share.maxProtein.roundToInt()
        progressFett = share.maxFett.roundToInt()


        initViews()



        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initViews()
    {

        // Buttons:
        btnAuto = view.findViewById(R.id.dialog_bodysettings_nutrition_btn_auto)
        btnAbort = view.findViewById(R.id.dialog_bodysettings_nutrition_btn_abort)
        btnSave = view.findViewById(R.id.dialog_bodysettings_nutrition_btn_save)

        // Listener:
        btnAuto.setOnClickListener(this)
        btnAbort.setOnClickListener(this)
        btnSave.setOnClickListener(this)



        // TextViews:
        tvCarbs = view.findViewById(R.id.dialog_bodysettings_nutrition_tv_carbs)
        tvProtein = view.findViewById(R.id.dialog_bodysettings_nutrition_tv_protein)
        tvFett = view.findViewById(R.id.dialog_bodysettings_nutrition_tv_fett)

        tvCarbsPercent = view.findViewById(R.id.dialog_bodysettings_nutrition_tv_carb_percent)
        tvProteinPercent = view.findViewById(R.id.dialog_bodysettings_nutrition_tv_protein_percent)
        tvFettPercent = view.findViewById(R.id.dialog_bodysettings_nutrition_tv_fett_percent)

        // SeekBar
        skCarb = view.findViewById(R.id.dialog_bodysettings_nutrition_sk_carb)
        skProtein = view.findViewById(R.id.dialog_bodysettings_nutrition_sk_protein)
        skFett = view.findViewById(R.id.dialog_bodysettings_nutrition_sk_fett)

        skCarb.max = 20
        //skCarb.min = 0
        skCarb.progress = progressCarb/5

        skProtein.max = 20
        //skProtein.min = 0
        skProtein.progress = progressProtein/5

        skFett.max = 20
        //skFett.min = 0
        skFett.progress = progressFett/5

        // Listener SeekBar
        skCarb.setOnSeekBarChangeListener(this)
        skProtein.setOnSeekBarChangeListener(this)
        skFett.setOnSeekBarChangeListener(this)


        //EditText
        etKcal = view.findViewById(R.id.dialog_bodysettings_nutrition_et_kcal)
        etKcal.editText!!.setText("$maxKcal")
        etKcal.editText!!.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {
                if(etKcal.editText!!.text.isNullOrEmpty())
                {
                    maxKcal = 0

                }
                else
                {
                    maxKcal = etKcal.editText!!.text.toString().toFloat().roundToInt()
                }

                updateViews()
            }

        })

        tvCarbsPercent.text = "$progressCarb %"
        tvProteinPercent.text = "$progressProtein %"
        tvFettPercent.text = "$progressFett %"

        tvCarbs.text = "${(((progressCarb/100f)*maxKcal)/4.1f).roundToInt()} g"
        tvProtein.text = "${(((progressProtein/100f)*maxKcal)/4.1f).roundToInt()} g"
        tvFett.text = "${(((progressFett/100f)*maxKcal)/9.2f).roundToInt()} g"

    }



    private fun updateViews()
    {
       // tvCarbsPercent.text = "${progressCarb} %"
        tvCarbs.text = "${(((progressCarb/100f)*maxKcal)/4.1f).roundToInt()} g"
        //tvProteinPercent.text = "${progressProtein} %"
        tvProtein.text = "${(((progressProtein/100f)*maxKcal)/4.1f).roundToInt()} g"
        //tvFettPercent.text = "${progressFett} %"
        tvFett.text = "${(((progressFett/100f)*maxKcal)/9.2f).roundToInt()} g"
    }


    private fun checkIfToMuch()
    {
        if((progressCarb+progressProtein+progressFett)!=100)
        {
            skCarb.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.Error))
            skProtein.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.Error))
            skFett.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.Error))

            skCarb.thumbTintList = ContextCompat.getColorStateList(context, R.color.Error)
            skProtein.thumbTintList = ContextCompat.getColorStateList(context, R.color.Error)
            skFett.thumbTintList = ContextCompat.getColorStateList(context, R.color.Error)
        }
        else
        {
            skCarb.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorAccent))
            skProtein.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorAccent))
            skFett.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorAccent))

            skCarb.thumbTintList = ContextCompat.getColorStateList(context, R.color.colorAccent)
            skProtein.thumbTintList = ContextCompat.getColorStateList(context, R.color.colorAccent)
            skFett.thumbTintList = ContextCompat.getColorStateList(context, R.color.colorAccent)
        }
    }

    private fun startSavingProcess()
    {
        if((progressCarb+progressProtein+progressFett)==100)
        {
            share.setNewMaxKcal(maxKcal.toFloat())
            share.setNewMaxCarb(progressCarb.toFloat())
            share.setNewMaxProtein(progressProtein.toFloat())
            share.setNewMaxFett(progressFett.toFloat())

            if(mListener!=null)
            {
                mListener.setOnDialogNutritionListener()
                alertDialog.dismiss()
            }

        }
        else
        {
            Toast.makeText(context,"Nährwertzusammenstellung muss genau 100% ergeben",Toast.LENGTH_SHORT).show()
        }

    }



    // Implementierte Methoden:
    // SeekBar
    override fun onProgressChanged(sk: SeekBar?, p1: Int, p2: Boolean)
    {
        if(sk == skCarb)
        {
            progressCarb = p1*5
            tvCarbsPercent.text = "$progressCarb %"
            tvCarbs.text = "${(((progressCarb/100f)*maxKcal)/4.1f).roundToInt()} g"

        }
        else if (sk == skProtein)
        {
            progressProtein = p1*5
            tvProteinPercent.text = "$progressProtein %"
            tvProtein.text = "${(((progressProtein/100f)*maxKcal)/4.1f).roundToInt()} g"
        }
        else
        {
            progressFett = p1*5
            tvFettPercent.text = "$progressFett %"
            tvFett.text = "${(((progressFett/100f)*maxKcal)/9.2f).roundToInt()} g"
        }
        checkIfToMuch()

    }
    override fun onStartTrackingTouch(p0: SeekBar?)
    {

    }
    override fun onStopTrackingTouch(p0: SeekBar?)
    {

    }

    // Buttons:
    override fun onClick(btn: View?)
    {
        if(btn == btnAuto)
        {
            var dialog = DialogAutoCalcKcal(context)
            dialog.setOnDialogAutoCalcKcalListener(object: DialogAutoCalcKcal.OnDialogAutoCalcKcalListener{
                override fun setOnDialogAutoCalcKcalListener(arg: String, value: Float) {
                    // Berechnen der Kcal:
                    if(bodyViewModel.getLocalBody() == null)
                    {
                        Toast.makeText(context,"Du musst mindestens einen Bodyeintrag gemacht haben",Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        var weight = bodyViewModel.getLocalBody()!!.weight
                        var height = share.userHeight
                        var age = helper.getYearsBetweenDates(helper.getDateFromString(share.bday),helper.getCurrentDate())
                        var kcal:Float = 0f
                        if(share.sex == "Mann")
                        {
                            kcal = ((10*weight)+ (6.25*height) - (5f*age) + 5).toFloat()
                            kcal *= value
                            maxKcal = kcal.roundToInt()
                            maxKcal += (share.aimWeightLoss * 1000f).roundToInt()
                            etKcal.editText!!.setText(maxKcal.toString())
                        }
                        else if (share.sex == "Frau")
                        {
                            kcal = ((10*weight)+ (6.25*height) - (5f*age) -161).toFloat()
                            kcal *= value
                            maxKcal = kcal.roundToInt()
                            maxKcal += (share.aimWeightLoss * 1000f).roundToInt()
                            etKcal.editText!!.setText(maxKcal.toString())
                        }
                    }

                }

            })

        }
        else if(btn == btnSave)
        {
            startSavingProcess()
        }
        else
        {
            alertDialog.dismiss()
        }

    }


    // Interface
    interface OnDialogNutritionListener
    {
        fun setOnDialogNutritionListener()
    }

    fun setOnDialogNutritionListener(mListener:OnDialogNutritionListener)
    {
        this.mListener = mListener
    }
}