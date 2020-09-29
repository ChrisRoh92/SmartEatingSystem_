package de.rohnert.smarteatingsystem.frontend.foodtracker.dialogs

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButtonToggleGroup
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.backend.databases.daily_database.MealEntry
import de.rohnert.smarteatingsystem.backend.databases.daily_database.helper.CalcedFood
import de.rohnert.smarteatingsystem.backend.repository.subrepositories.daily.DailyProcessor
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel.FoodViewModel
import de.rohnert.smarteatingsystem.frontend.foodtracker.animations.AnimationFoodPicker2
import de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel.TAG
import java.lang.ArithmeticException
import kotlin.math.roundToInt

class DialogMealEntryChanger(var foodViewModel: FoodViewModel, var context: Context, var calcedFood: CalcedFood, var meal:String)
{
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    private lateinit var animator: AnimationFoodPicker2

    private var helper: Helper = Helper()
    private var dailyProcessor = DailyProcessor()

    // Content
    var started = false
    var menge:Float = calcedFood.menge
    var restKcal:Int = 0
    var restCarbs:Int = 0
    var restProtein:Int = 0
    var restFett:Int = 0

    // View Elemente:
    //EditTest:
    lateinit var etMenge: TextInputLayout
    // TextViews
    lateinit var tvTitle: TextView
    lateinit var tvSubTitle: TextView
    lateinit var tvKcal: TextView
    lateinit var tvKcalRest: TextView
    lateinit var tvCarb: TextView
    lateinit var tvProtein: TextView
    lateinit var tvFett: TextView
    // ProgressBar:
    lateinit var barKcal: ProgressBar
    lateinit var barCarbs: ProgressBar
    lateinit var barProtein: ProgressBar
    lateinit var barFett: ProgressBar
    // Buttons:
    lateinit var btnSave: Button
    lateinit var btnAbort: Button

    // ToggleButtonGroup
    private lateinit var toggleGroup: MaterialButtonToggleGroup


    init {
        initDialog()
    }


    // Dialog initialisieren...
    fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_foodtracker_foodpicker, null)
        builder.setView(view)

        setRestNutritionValues()
        initViews()




        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    // Werte abrufen:
    private fun setRestNutritionValues()
    {

        foodViewModel.getCalcedFoodOfDay {dailyValues ->
            foodViewModel.getDailyMaxValues{maxValues->
                Log.d(TAG,"DialogMealEntryChanger - maxValues = $maxValues")
                Log.d(TAG,"DialogMealEntryChanger - dailyValues = $dailyValues")
                Log.d(TAG,"DialogMealEntryChanger - calcedFood.values = ${calcedFood.values}")

                restKcal = (maxValues[0].roundToInt() - dailyValues[0].roundToInt())+ calcedFood.values[0].roundToInt()
                restCarbs = maxValues[1].roundToInt() - dailyValues[1].roundToInt() + calcedFood.values[1].roundToInt()
                restProtein = maxValues[2].roundToInt() - dailyValues[2].roundToInt() + calcedFood.values[2].roundToInt()
                restFett = maxValues[3].roundToInt() - dailyValues[3].roundToInt() + calcedFood.values[3].roundToInt()
                initContent()
            }

        }


    }

    // Alle Views initialisieren...
    private fun initViews()
    {
        // TextViews...
        tvTitle = view.findViewById(R.id.foodpicker_tv_title)
        tvSubTitle = view.findViewById(R.id.foodpicker_tv_subtitle)

        tvKcalRest = view.findViewById(R.id.foodpicker_tv_kcal_rest)
        tvKcal = view.findViewById(R.id.foodpicker_tv_kcal)
        tvCarb = view.findViewById(R.id.foodpicker_tv_carbs)
        tvProtein = view.findViewById(R.id.foodpicker_tv_protein)
        tvFett = view.findViewById(R.id.foodpicker_tv_fett)


        // ProgressBar:
        barKcal = view.findViewById(R.id.foodpicker_bar_kcal)
        barCarbs = view.findViewById(R.id.foodpicker_bar_carbs)
        barProtein = view.findViewById(R.id.foodpicker_bar_protein)
        barFett = view.findViewById(R.id.foodpicker_bar_fett)

        // EditText:
        etMenge = view.findViewById(R.id.foodpicker_et_menge)
        etMenge.editText!!.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?)
            {
                if(started)
                {
                    if(etMenge.editText!!.text.isNotEmpty())
                    {
                        menge = etMenge.editText!!.text.toString().toFloat()
                        updateContent(menge)
                    }
                    else
                    {
                        menge = 0f
                        updateContent(menge)
                    }

                }
                else
                {
                    started = true
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {


            }

        })


        // Buttons:
        btnSave = view.findViewById(R.id.foodpicker_btn_save)
        btnAbort = view.findViewById(R.id.foodpicker_btn_abort)


        // ToggleGroup:
        toggleGroup = view.findViewById(R.id.foodpicker_toggle_group)
        val toggleIDs = arrayOf(R.id.toggle1,R.id.toggle2,R.id.toggle3,R.id.toggle4)
        val values = arrayOf("100","250","500","1000")
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            Log.d(TAG,"DialogMealEntryChanger - toggleIDs = $toggleIDs")
            Log.d(TAG,"DialogMealEntryChanger - toggleGroup checkedID = $checkedId")
            if(isChecked)
            {
                val pos = toggleIDs.indexOf(checkedId)
                if(pos == toggleIDs.lastIndex)
                {
                    if(restKcal > 0)
                        etMenge.editText!!.setText(calcRestKcal())
                    else
                        Toast.makeText(context,"Keine Kalorien mehr übrig!",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    etMenge.editText!!.setText(values[pos])
                }

            }


        }



        btnSave.setOnClickListener {
            // Über das Repository muss ein neues MealEntry in das Daily-Objekt gebracht werden, damit das auch direkt
            // gespeichert werden kann..
            //foodViewModel.addNewMealEntry(calcedFood.f.id,menge,meal)
            if(menge != calcedFood.menge) {
                if(menge != 0f) {
                    foodViewModel.updateMealEntry(MealEntry(calcedFood.id, calcedFood.f.id, menge),meal)

                    alertDialog.dismiss()
                } else {
                    Toast.makeText(context,"Bitte eine Menge größer 0 angeben...",Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context,"Bitte die Menge verändern...",Toast.LENGTH_SHORT).show()
            }

        }

        // Abbrechen ohne Eingabe
        btnAbort.setOnClickListener {
            alertDialog.dismiss()
        }


    }

    // InitContent:
    // Initial alle Views mit Werten belegen:
    private fun initContent()
    {

        // Titles:
        tvTitle.text = calcedFood.f.name
        tvSubTitle.text = calcedFood.f.category

        // EditTexts:
        //etMenge.editText!!.setText(helper.getFloatAsFormattedString(menge,"#.##"))


        // Diese Info muss aus dem ViewModel abgefragt werden...
        barKcal.max = restKcal
        barCarbs.max = restCarbs
        barProtein.max = restProtein
        barFett.max = restFett





        barFett.progress = 0
        barCarbs.progress = 0
        barProtein.progress = 0
        barFett.progress = 0
        tvKcal.text = ""
        tvKcalRest.text = ""
        tvCarb.text = ""
        tvProtein.text = ""
        tvFett.text = ""

        var pbList:ArrayList<ProgressBar> = arrayListOf(barCarbs,barProtein,barFett)
        var tvList:ArrayList<TextView> = arrayListOf(tvKcal,tvKcalRest,tvCarb,tvProtein,tvFett)
        var maxValues:ArrayList<Float> = arrayListOf(restKcal.toFloat(),restCarbs.toFloat(),restProtein.toFloat(),restFett.toFloat())
        var values:ArrayList<Float> = dailyProcessor.getCalcedFood(0,calcedFood.f,menge).values


        animator = AnimationFoodPicker2(context,barKcal,pbList,tvList,maxValues,values)

        // EditTexts:
        etMenge.editText!!.setText(helper.getFloatAsFormattedString(menge,"#.##"))


    }



    // Content updaten:
    private fun updateContent(value:Float)
    {
        var values:ArrayList<Float> = dailyProcessor.getCalcedFood(0,calcedFood.f,value).values
        if(animator!=null) animator.updateAnimation(values)
    }

    // Utils:
    private fun calcRestKcal():String
    {
        Log.d(TAG,"DialogMealEntryChanger calcRestKcal(): restKcal = $restKcal")
        Log.d(TAG,"DialogMealEntryChanger calcRestKcal(): calcedFood.values[0] = ${calcedFood.values[0]}")
        return if(restKcal <= 0)
            "0"
        else
            ((((restKcal.toFloat()*100f)/calcedFood.f.kcal)).roundToInt()).toString()

    }






}