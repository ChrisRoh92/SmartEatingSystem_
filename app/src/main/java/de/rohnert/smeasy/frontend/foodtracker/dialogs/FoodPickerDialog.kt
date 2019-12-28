package de.rohnert.smeasy.frontend.foodtracker.dialogs

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import de.rohnert.smeasy.backend.helper.Helper
import de.rohnert.smeasy.backend.repository.subrepositories.daily.DailyProcessor
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smeasy.frontend.foodtracker.FoodViewModel2
import de.rohnert.smeasy.frontend.foodtracker.animations.AnimationFoodPicker2
import kotlin.math.roundToInt


class FoodPickerDialog(var foodViewModel: FoodViewModel2, var context: Context, var food: ExtendedFood, var meal:String)
     {



    lateinit var builder: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater
    var helper: Helper = Helper()
    private var dailyProcessor = DailyProcessor(context)
    lateinit var animator: AnimationFoodPicker2

    // View Elemente:
    //EditTest:
    lateinit var etMenge:TextInputLayout
    // TextViews
    lateinit var tvTitle:TextView
    lateinit var tvSubTitle:TextView
    lateinit var tvKcal:TextView
    lateinit var tvKcalRest:TextView
    lateinit var tvCarb:TextView
    lateinit var tvProtein:TextView
    lateinit var tvFett:TextView
    // ProgressBar:
    lateinit var barKcal:ProgressBar
    lateinit var barCarbs:ProgressBar
    lateinit var barProtein:ProgressBar
    lateinit var barFett:ProgressBar
   // Buttons:
    lateinit var btnSave:Button
    lateinit var btnAbort:Button

    // Content
    var started = false
    var menge:Float = 100f
    var restKcal:Int = 0
    var restCarbs:Int = 0
    var restProtein:Int = 0
    var restFett:Int = 0


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
        initContent()





        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    fun setRestNutritionValues()
    {
        restKcal = foodViewModel.getDailyMaxValues()[0].roundToInt() - foodViewModel.getDailyValues()[0].roundToInt()
        restCarbs = foodViewModel.getDailyMaxValues()[1].roundToInt() - foodViewModel.getDailyValues()[1].roundToInt()
        restProtein = foodViewModel.getDailyMaxValues()[2].roundToInt() - foodViewModel.getDailyValues()[2].roundToInt()
        restFett = foodViewModel.getDailyMaxValues()[3].roundToInt() - foodViewModel.getDailyValues()[3].roundToInt()

    }

    // Alle Views initialisieren...
    fun initViews()
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


        // Listener...


        btnSave.setOnClickListener({
            // Über das Repository muss ein neues MealEntry in das Daily-Objekt gebracht werden, damit das auch direkt
            // gespeichert werden kann..
            if(menge != 0f)
            {
                foodViewModel.addNewMealEntry(food.id,menge,meal)

                alertDialog.dismiss()
            }
            else
            {
                Toast.makeText(context,"Bitte eine Menge größer 0 angeben...",Toast.LENGTH_SHORT).show()
            }

        })

        // Abbrechen ohne Eingabe
        btnAbort.setOnClickListener({
            alertDialog.dismiss()
        })





    }

    // Initial alle Views mit Werten belegen:
    private fun initContent()
    {

        // Titles:
        tvTitle.text = food.name
        tvSubTitle.text = food.category

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
        var values:ArrayList<Float> = dailyProcessor.getCalcedFood(0,food,menge).values


        animator = AnimationFoodPicker2(context,barKcal,pbList,tvList,maxValues,values)

        // EditTexts:
        etMenge.editText!!.setText(helper.getFloatAsFormattedString(menge,"#.##"))


        }






        // Über das ViewModel muss abgecheckt werden, wieviel noch verbraucht werden darf...
        // Darüber muss noch die ProgressBars, die Reste gefüllt werden...






    fun updateContent(value:Float)
    {
        var values:ArrayList<Float> = dailyProcessor.getCalcedFood(0,food,value).values
        if(animator!=null) animator.updateAnimation(values)

    }

    /*// Dialog zur Aufnahme der neuen Menge:
    fun createAlertDialog()
    {
        var builder:AlertDialog.Builder = AlertDialog.Builder(context)
        var dialog:AlertDialog
        var et:EditText = EditText(context)
        builder.setMessage("Bitte die Menge in g/ml angeben")
        builder.setTitle("Manuelle Eingabe")
        et.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        builder.setView(et)
        // Positiver Button:
        builder.setPositiveButton("OK", DialogInterface.OnClickListener {
                dialogInterface, i ->  if(!et.text.toString().isEmpty())
        {
            updateContent(et.text.toString().toFloat())
            if(et.text.toString().toInt() > skMenge.max)
            {
                skMenge.max = et.text.toString().toInt() + 500
                skMenge.progress = et.text.toString().toInt()
            }
            else
            {
                skMenge.progress = et.text.toString().toInt()
            }



        }
            else
        {
            Toast.makeText(context,"Bitte einen Wert eintragen",Toast.LENGTH_SHORT).show()
        }

        })

        builder.setNegativeButton("Abbrechen", DialogInterface.OnClickListener {
                dialogInterface, i ->


        })

        dialog = builder.create()
        dialog.show()

    }*/

    /*fun createInputDialog()
    {
        var unit = ""
        if(food.unit.contains("g"))
        {
            unit = "g"
        }
        else
        {
            unit = "ml"
        }

        var dialog = DialogSingleLineInput("Bitte die Menge in $unit angeben","Manuelle Eingabe",context,InputType.TYPE_CLASS_NUMBER)
        dialog.onDialogClickListener(object : OnDialogListener {
            override fun onDialogClickListener(export: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDialogClickListener(export: Float) {
                if(export > skMenge.max)
                {
                    skMenge.max = export.roundToInt() + 500
                    skMenge.progress = export.roundToInt()
                }
                else
                {
                    skMenge.progress = export.roundToInt()
                }
                updateContent(export)
            }

        })*/
    }



























