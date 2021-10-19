package de.rohnert.smarteatingsystem.frontend.foodtracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import de.rohnert.smarteatingsystem.R
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel.FoodViewModel
import de.rohnert.smarteatingsystem.utils.dialogs.DialogSingleLineInput

class FoodCreatorDialog(var foodViewModel: FoodViewModel, var context: Context) : AdapterView.OnItemSelectedListener {


    lateinit var builder: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater


    // View-Elemente:
    lateinit var et_name:TextInputLayout
    lateinit var et_kcal:TextInputLayout
    lateinit var et_carbs:TextInputLayout
    lateinit var et_protein:TextInputLayout
    lateinit var et_fett:TextInputLayout

    // RadioButton:
    lateinit var rbtn_100g:RadioButton
    lateinit var rbtn_100ml:RadioButton

    // RadioGroup:
    lateinit var rbtn_group:RadioGroup

    // Spinner:
    lateinit var spinner:Spinner

    // Button:
    lateinit var btn_abort:Button
    lateinit var btn_save:Button

    // Content:
    lateinit var groupList:ArrayList<String>



    init {
        initDialog()
    }

    // Dialog initialisieren...
    fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_foodlist_foodcreator, null)
        builder.setView(view)

        initViews()
        initContent()

        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    // Alle Views initialisieren:
    fun initViews()
    {
        // TextInputLayout:
        et_name = view.findViewById(R.id.foodcreator_et_name)
        et_kcal = view.findViewById(R.id.foodcreator_et_kcal)
        et_carbs = view.findViewById(R.id.foodcreator_et_carbs)
        et_protein = view.findViewById(R.id.foodcreator_et_protein)
        et_fett = view.findViewById(R.id.foodcreator_et_fett)

        // RadioButton:
        rbtn_100g = view.findViewById(R.id.foodcreator_rbtn_100g)
        rbtn_100ml = view.findViewById(R.id.foodcreator_rbtn_100ml)

        // RadioGruppe:
        rbtn_group = view.findViewById(R.id.foodcreator_radioGroup)


        // Spinner:
        spinner = view.findViewById(R.id.foodcreator_spinner)


        // Button:
        btn_abort = view.findViewById(R.id.foodcreator_btn_abort)
        btn_save = view.findViewById(R.id.foodcreator_btn_save)


        // Button Listener:
        btn_abort.setOnClickListener {
            alertDialog.dismiss()
        }

        btn_save.setOnClickListener {
            createNewFood()
        }
    }

    // Content initialisieren:
    fun initContent()
    {
        groupList = ArrayList()
        groupList.addAll(foodViewModel.getFoodCategories())
        groupList.add("Neue Lebensmittel Gruppe erstellen...")

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, groupList)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner.adapter = aa

        spinner.onItemSelectedListener = this






    }

    // Über diese Funktion wird ein neues Food erstellt...
    fun createNewFood()
    {
        if(!spinner.selectedItem.toString().equals("Neue Lebensmittel Gruppe erstellen...")
            and et_name.editText!!.text.toString().isNotEmpty()
            and et_kcal.editText!!.text.toString().isNotEmpty()
            and et_carbs.editText!!.text.toString().isNotEmpty()
            and et_protein.editText!!.text.toString().isNotEmpty()
            and et_fett.editText!!.text.toString().isNotEmpty()
        )
        {
            var einheit = ""
            if(rbtn_group.checkedRadioButtonId == R.id.foodcreator_rbtn_100g)
            {
                einheit = "100g"
            }

            else
            {
                einheit = "100ml"
            }


            // TODO: Bitte Portionsangaben berücksichtigen
            foodViewModel.addNewFood(spinner.selectedItem.toString(),
                                        et_name.editText!!.text.toString(),
                                        einheit,"","",
                                        et_kcal.editText!!.text.toString().toFloat(),
                                        et_carbs.editText!!.text.toString().toFloat(),
                                        et_protein.editText!!.text.toString().toFloat(),
                                        et_fett.editText!!.text.toString().toFloat(),
                                        portionName = ArrayList<String>(),
                                        portionSize =  ArrayList<Double>())


            alertDialog.dismiss()

        }
        else
        {
            Toast.makeText(context,"Bitte alle Felder ausfüllen...",Toast.LENGTH_SHORT).show()
        }

    }



    // Dialog zum erstellen einer neuen Lebensmittelgruppe...
    fun initAlertDialog()
    {
        var dialog = DialogSingleLineInput("Name der Lebensmittelgruppe","Neue Lebensmittelgruppe eintragen",context,InputType.TYPE_CLASS_TEXT,"")
        dialog.onDialogClickListener(object :DialogSingleLineInput.OnDialogListener{
            override fun onDialogClickListener(export: String)
            {
                addNewGroupToList(export)

            }

            override fun onDialogClickListener(export: Float)
            {

            }

        })


    }

    // Über diese Methode wird eine neue Gruppe in das Dialog eingearbeitet
    // Wird aber das Lebensmittel nicht gespeichert, wird aber auch nicht die neue Lebensmittelgruppe gespeichert...
    fun addNewGroupToList(input:String)
    {
        groupList.set(groupList.lastIndex,input)
        groupList.add("Neue Lebensmittel Gruppe erstellen...")

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, groupList)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner.adapter = aa

        spinner.onItemSelectedListener = this
        spinner.setSelection(groupList.lastIndex - 1)
    }






    // Implementierte Methoden:
    // Zur Spinner:
    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if(p2 == groupList.lastIndex)
        {
            Toast.makeText(context,"Eine Lebensmittelgruppe wird erstellen...",Toast.LENGTH_SHORT).show()
            initAlertDialog()
        }
    }

}