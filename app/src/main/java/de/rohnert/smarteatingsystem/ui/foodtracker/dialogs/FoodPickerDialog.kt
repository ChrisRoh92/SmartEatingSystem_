package de.rohnert.smarteatingsystem.ui.foodtracker.dialogs

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smarteatingsystem.data.helper.Helper
import de.rohnert.smarteatingsystem.ui.foodtracker.animations.AnimationFoodPicker2
import de.rohnert.smarteatingsystem.ui.foodtracker.viewmodel.FoodViewModel
import de.rohnert.smarteatingsystem.ui.foodtracker.viewmodel.TAG
import de.rohnert.smarteatingsystem.ui.foodtracker.viewmodel.getCalcedFood
import de.rohnert.smarteatingsystem.utils.dialogs.DialogTwoLineInput
import kotlin.math.roundToInt


class FoodPickerDialog(var foodViewModel: FoodViewModel, var context: Context, var food: ExtendedFood, var meal:String) :
    View.OnClickListener {


    ////////////////////////////////////////////////////////////
    // Allgemeine Variablen:
    // Dialog Stuiff
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater
    // Other Stuff:
    private var helper: Helper = Helper()
    private lateinit var animator: AnimationFoodPicker2

    ////////////////////////////////////////////////////////////
    // View Elemente:
    //EditTest:
    private lateinit var etMenge:TextInputLayout
    // TextViews
    private lateinit var tvTitle:TextView
    private lateinit var tvSubTitle:TextView
    private lateinit var tvKcal:TextView
    private lateinit var tvKcalRest:TextView
    private lateinit var tvCarb:TextView
    private lateinit var tvProtein:TextView
    private lateinit var tvFett:TextView
    // ProgressBar:
    private lateinit var barKcal:ProgressBar
    private lateinit var barCarbs:ProgressBar
    private lateinit var barProtein:ProgressBar
    private lateinit var barFett:ProgressBar
   // Buttons:
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button

    // ImageButton:
    private lateinit var btnEdit:ImageButton

    // Portionsangaben
    private lateinit var btnNewPortion:ImageButton
    private lateinit var etSpinnerPortion:TextInputLayout
    private lateinit var spinner:AutoCompleteTextView
    private var portionName = arrayListOf("Klein","Mittel","Groß")
    private var portionValue = arrayListOf(100.0,250.0,500.0)

    // Content
    private var started = false
    private var menge:Float = 100f
    private var restKcal:Int = 0
    private var restCarbs:Int = 0
    private var restProtein:Int = 0
    private var restFett:Int = 0

    // ToggleButtonGroup
    private lateinit var toggleGroup: MaterialButtonToggleGroup


    ////////////////////////////////////////////////////////////
    // Einstiegspunkt
    init {
        initDialog()
    }


    ////////////////////////////////////////////////////////////
    // Dialog und Views initialisieren
    private fun initDialog()
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

        // Listener...

        toggleGroup.setOnClickListener {
            // Feststellen welcher Button geklickt wurde
            Log.d(TAG,"FoodPickerDialog - toggleGroup.OnClickListener ID = ${toggleGroup.checkedButtonId}")
            // Alle Abwählen

            // Geklickten Button markieren

        }


        // Ändern Eintrag:
        btnEdit = view.findViewById(R.id.foodpicker_btn_edit)
        btnEdit.setOnClickListener {
        // TODO: Dialog zum Ändern des Eintrags erstellen
             }

        // Portionsangaben:
        btnNewPortion = view.findViewById(R.id.foodpicker_btn_portion)
        btnNewPortion.setOnClickListener {
            val dialog = DialogTwoLineInput("Neue Portion","Bitte Name und Wert eintragen",context, InputType.TYPE_CLASS_TEXT,InputType.TYPE_CLASS_NUMBER,"","")
            dialog.setOnDialogClickListener(object:DialogTwoLineInput.OnDialogListener{
                override fun setOnDialogClickListener(export1: String, export2: String) {
                    food.portionName.add(export1)
                    food.portionValue.add(export2.toDouble())
                    foodViewModel.updateFood(food)
                    if(!spinner.isEnabled)
                    {
                        initSpinner()
                        spinner.isEnabled = true

                    }

                }

            })
        }

        etSpinnerPortion = view.findViewById(R.id.foodpicker_spinner)


        spinner = etSpinnerPortion.editText as AutoCompleteTextView

        if(food.portionName.isEmpty())
        {
            spinner.isEnabled = false
        }
        else
        {
            initSpinner()
        }




        // Eintrag speichern
        btnSave.setOnClickListener(this)

        // Abbrechen ohne Eingabe
        btnAbort.setOnClickListener(this)





    }

    private fun initSpinner()
    {
        spinner.setAdapter( ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,food.portionName))
        spinner.setOnItemClickListener { parent, view, position, id ->
            etMenge!!.editText!!.setText(food.portionValue[position].toString())
        }
    }

    ////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////
    // Content Handling:
    // Berechnen der noch für den Tag übrigen Werte:
    private fun setRestNutritionValues()
    {

        foodViewModel.getCalcedFoodOfDay{ dailyValues ->

            foodViewModel.getDailyMaxValues{maxValues ->

                restKcal = maxValues[0].roundToInt() - dailyValues[0].roundToInt()
                restCarbs = maxValues[1].roundToInt() - dailyValues[1].roundToInt()
                restProtein = maxValues[2].roundToInt() - dailyValues[2].roundToInt()
                restFett = maxValues[3].roundToInt() - dailyValues[3].roundToInt()
                initContent()
            }

        }


    }
    // Initial alle Views mit Werten belegen:
    private fun initContent()
    {

        // Titles:
        tvTitle.text = food.name
        tvSubTitle.text = food.category

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
        var values:ArrayList<Float> = getCalcedFood(0,food,menge).values


        animator = AnimationFoodPicker2(context,barKcal,pbList,tvList,maxValues,values)

        // EditTexts:
        etMenge.editText!!.setText(helper.getFloatAsFormattedString(menge,"#.##"))


    }
    // Content nach neuer Eingabe updaten:
    private fun updateContent(value:Float)
    {
        var values:ArrayList<Float> = getCalcedFood(0,food,value).values
        if(animator!=null) animator.updateAnimation(values)

    }
    ////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////
    // Button Click
    override fun onClick(v: View?) {
        if(v == btnSave)
            saveButtonToViewModel()
        else
            alertDialog.dismiss()
    }
    // Wird aufgerufen, wenn User auf Save Button klickt
    private fun saveButtonToViewModel()
    {
        if(menge != 0f)
        {
            foodViewModel.addNewMealEntry(food.id,menge,meal, food.kcal*(menge/1000.0F), food.carb*(menge/1000.0F), food.protein*(menge/1000.0F), food.fett*(menge/1000.0F))

            alertDialog.dismiss()
        }
        else
        {
            Toast.makeText(context,"Bitte eine Menge größer 0 angeben...",Toast.LENGTH_SHORT).show()
        }
    }
    ////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////
    // Utils:
    private fun calcRestKcal():String
    {
        return if(restKcal <= 0)
            "0"
        else
            ((((restKcal.toFloat()*100f)/food.kcal)).roundToInt()).toString()
    }
    ///////////////////////////////////////////////////////////
}



























