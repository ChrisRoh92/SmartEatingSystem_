package de.rohnert.smeasy.frontend.foodtracker.dialogs

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import com.example.roomdatabaseexample.backend.repository.subrepositories.daily.DailyProcessor
import de.rohnert.smeasy.R
import de.rohnert.smeasy.frontend.foodtracker.FoodViewModel
import de.rohnert.smeasy.frontend.foodtracker.FoodViewModel2
import de.rohnert.smeasy.frontend.foodtracker.animations.AnimationFoodPicker
import de.rohnert.smeasy.helper.dialogs.DialogSingleLineInput
import de.rohnert.smeasy.helper.dialogs.DialogSingleLineInput.OnDialogListener
import kotlin.math.roundToInt


class FoodPickerDialog(var foodViewModel: FoodViewModel2, var context: Context, var food: Food, var meal:String) :
    SeekBar.OnSeekBarChangeListener {



    lateinit var builder: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater
    var helper: Helper = Helper()
    private var dailyProcessor = DailyProcessor(context)
    lateinit var animator: AnimationFoodPicker

    // View Elemente:
    // TextViews
    lateinit var tvTitle:TextView
    lateinit var tvSubTitle:TextView
    lateinit var tvMenge:TextView
    lateinit var tvKcal:TextView
    lateinit var tvCarb:TextView
    lateinit var tvProtein:TextView
    lateinit var tvFett:TextView

    // ProgressBar:
    lateinit var barKcal:ProgressBar
    lateinit var barCarbs:ProgressBar
    lateinit var barProtein:ProgressBar
    lateinit var barFett:ProgressBar
    // SeekBar:
    lateinit var skMenge:SeekBar
    // Buttons:
    lateinit var btnMenge:Button
    lateinit var btnSave:Button
    lateinit var btnAbort:Button

    // Content
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

        tvMenge = view.findViewById(R.id.foodpicker_tv_menge)
        tvKcal = view.findViewById(R.id.foodpicker_tv_kcal)
        tvCarb = view.findViewById(R.id.foodpicker_tv_carbs)
        tvProtein = view.findViewById(R.id.foodpicker_tv_protein)
        tvFett = view.findViewById(R.id.foodpicker_tv_fett)


        // ProgressBar:
        barKcal = view.findViewById(R.id.foodpicker_bar_kcal)
        barCarbs = view.findViewById(R.id.foodpicker_bar_carbs)
        barProtein = view.findViewById(R.id.foodpicker_bar_protein)
        barFett = view.findViewById(R.id.foodpicker_bar_fett)

        // SeekBar:
        skMenge = view.findViewById(R.id.foodpicker_sk_menge)

        // Buttons:
        btnMenge = view.findViewById(R.id.foodpicker_btn_menge)
        btnSave = view.findViewById(R.id.foodpicker_btn_save)
        btnAbort = view.findViewById(R.id.foodpicker_btn_abort)


        // Listener...
        skMenge.setOnSeekBarChangeListener(this)

        btnMenge.setOnClickListener(View.OnClickListener
        {
            createInputDialog()
        })

        btnSave.setOnClickListener(View.OnClickListener {
            // Über das Repository muss ein neues MealEntry in das Daily-Objekt gebracht werden, damit das auch direkt
            // gespeichert werden kann..
            foodViewModel.addNewMealEntry(food.id,skMenge.progress.toFloat(),meal)

            alertDialog.dismiss()
        })

        // Abbrechen ohne Eingabe
        btnAbort.setOnClickListener(View.OnClickListener {
            alertDialog.dismiss()
        })





    }

    // Initial alle Views mit Werten belegen:
    fun initContent()
    {

        skMenge.max = 500
        skMenge.progress = 100
        tvTitle.text = food.name
        tvSubTitle.text = food.category




        tvMenge.text = "${skMenge.progress} g / ${skMenge.max} g"
        // Diese Info muss aus dem ViewModel abgefragt werden...
        barKcal.max = restKcal
        barCarbs.max = restCarbs
        barProtein.max = restProtein
        barFett.max = restFett



        /*barFett.progress = food.kcal.roundToInt()
        barCarbs.progress = food.carb.roundToInt()
        barProtein.progress = food.protein.roundToInt()
        barFett.progress = food.fett.roundToInt()
        tvKcal.text = "${food.kcal} Kcal / $restKcal Kcal"
        tvCarb.text = "${food.carb} g / $restCarbs g"
        tvProtein.text = "${food.protein} g / $restProtein g"
        tvFett.text = "${food.fett} g / $restFett g"*/

        barFett.progress = 0
        barCarbs.progress = 0
        barProtein.progress = 0
        barFett.progress = 0
        tvKcal.text = ""
        tvCarb.text = ""
        tvProtein.text = ""
        tvFett.text = ""

        var pbList:ArrayList<ProgressBar> = arrayListOf(barKcal,barCarbs,barProtein,barFett)
        var tvList:ArrayList<TextView> = arrayListOf(tvKcal,tvCarb,tvProtein,tvFett)
        var maxValues:ArrayList<Float> = arrayListOf(restKcal.toFloat(),restCarbs.toFloat(),restProtein.toFloat(),restFett.toFloat())
        var values:ArrayList<Float> = dailyProcessor.getCalcedFood(0,food,skMenge.progress.toFloat()).values


        animator = AnimationFoodPicker(context,pbList,tvList,maxValues,values)


        }






        // Über das ViewModel muss abgecheckt werden, wieviel noch verbraucht werden darf...
        // Darüber muss noch die ProgressBars, die Reste gefüllt werden...






    fun updateContent(value:Float)
    {
        var values:ArrayList<Float> = dailyProcessor.getCalcedFood(0,food,skMenge.progress.toFloat()).values
        animator.updateAnimation(values)

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

    fun createInputDialog()
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

        })
    }






















    // Implementierte Methoden:
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean)
    {
        tvMenge.text = "$p1 g / ${skMenge.max} g"
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        updateContent(p0!!.progress.toFloat())

    }




}