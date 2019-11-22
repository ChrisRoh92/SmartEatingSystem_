package de.rohnert.smeasy.frontend.foodtracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import de.rohnert.smeasy.R
import com.google.android.material.textfield.TextInputLayout
import de.rohnert.smeasy.frontend.foodtracker.FoodViewModel2

class FoodCreatorDialog(var foodViewModel: FoodViewModel2, var context: Context) : AdapterView.OnItemSelectedListener {


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
        btn_abort.setOnClickListener(View.OnClickListener {
            alertDialog.dismiss()
        })

        btn_save.setOnClickListener(View.OnClickListener {
            createNewFood()
        })
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
        spinner!!.adapter = aa

        spinner!!.onItemSelectedListener = this






    }

    // Über diese Funktion wird ein neues Food erstellt...
    fun createNewFood()
    {
        if(!spinner.selectedItem.toString().equals("Neue Lebensmittel Gruppe erstellen...")
            and !et_name.editText!!.text.toString().isEmpty()
            and !et_kcal.editText!!.text.toString().isEmpty()
            and !et_carbs.editText!!.text.toString().isEmpty()
            and !et_protein.editText!!.text.toString().isEmpty()
            and !et_fett.editText!!.text.toString().isEmpty())
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



            foodViewModel.addNewFood(spinner.selectedItem.toString(),
                                        et_name.editText!!.text.toString(),
                                        einheit,
                                        et_kcal.editText!!.text.toString().toFloat(),
                                        et_carbs.editText!!.text.toString().toFloat(),
                                        et_protein.editText!!.text.toString().toFloat(),
                                        et_fett.editText!!.text.toString().toFloat())


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
        var builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(context)
        var dialog: androidx.appcompat.app.AlertDialog
        var et = EditText(context)
        builder.setMessage("Name der neuen Lebensmittelgruppe")
        builder.setTitle("Lebensmittel erstellen")
        et.inputType = (InputType.TYPE_CLASS_TEXT)
        builder.setView(et)
        // Positiver Button:
        builder.setPositiveButton("OK", DialogInterface.OnClickListener {
                dialogInterface, i ->  if(!et.text.toString().isEmpty())
        {
            addNewGroupToList(et.text.toString())




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
        spinner!!.adapter = aa

        spinner!!.onItemSelectedListener = this
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