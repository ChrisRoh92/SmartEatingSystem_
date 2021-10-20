package de.rohnert.smarteatingsystem.ui.foodtracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import de.rohnert.smarteatingsystem.R

class DialogFoodListSorter(var context: Context) : View.OnClickListener {


    lateinit var builder: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater

    // View Elemente:
    // Buttons
    private lateinit var btnChange:Button
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button
    // RadioButtons:
    private lateinit var rbtnStandard:RadioButton
    private lateinit var rbtnName:RadioButton
    private lateinit var rbtnGruppe:RadioButton
    private lateinit var rbtnKcal:RadioButton
    private lateinit var rbtnCarbs:RadioButton
    private lateinit var rbtnProtein:RadioButton
    private lateinit var rbtnFett:RadioButton
    // RadioButtonGroup
    private lateinit var rbtnGroup: RadioGroup

    // Logik Reihenfolge:
    private var aufsteigend = true

    // Interface
    private lateinit var mListener: OnDialogClickListener



    init {
        initDialog()
    }

    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_foodlist_sorter, null)
        builder.setView(view)

        initViews()

        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initViews()
    {
        btnChange = view.findViewById(R.id.dialog_sorter_btn_umkehren)
        btnSave = view.findViewById(R.id.dialog_sorter_btn_save)
        btnAbort = view.findViewById(R.id.dialog_sorter_btn_abort)

        // btn Listener:
        btnChange.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnAbort.setOnClickListener(this)

        // RadioGroup:
        rbtnGroup = view.findViewById(R.id.dialog_sorter_rbtn_group)

        // RadioButtons:
        rbtnStandard = view.findViewById(R.id.dialog_sorter_rbtn_standard)
        rbtnName = view.findViewById(R.id.dialog_sorter_rbtn_name)
        rbtnGruppe = view.findViewById(R.id.dialog_sorter_rbtn_gruppe)
        rbtnKcal = view.findViewById(R.id.dialog_sorter_rbtn_kcal)
        rbtnCarbs = view.findViewById(R.id.dialog_sorter_rbtn_carbs)
        rbtnProtein = view.findViewById(R.id.dialog_sorter_rbtn_proteine)
        rbtnFett = view.findViewById(R.id.dialog_sorter_rbtn_fett)

        // Set Activ:


    }

    // Rbtn Text ändern...
    private fun changeTexts()
    {
        if(aufsteigend)
        {
            aufsteigend = false
            rbtnName.text = "Name (Z-A)"
            rbtnGruppe.text = "Gruppe (Z-A)"
            rbtnKcal.text = "Kalorien (Höchste zuerst)"
            rbtnCarbs.text = "Kohlenhydrate (Höchste zuerst)"
            rbtnProtein.text = "Protein (Höchste zuerst)"
            rbtnFett.text = "Fett (Höchste zuerst)"
        }
        else
        {

            aufsteigend = true
            rbtnName.text = "Name (A-Z)"
            rbtnGruppe.text = "Gruppe (A-Z)"
            rbtnKcal.text = "Kalorien (Niedrigste zuerst)"
            rbtnCarbs.text = "Kohlenhydrate (Niedrigste zuerst)"
            rbtnProtein.text = "Protein (Niedrigste zuerst)"
            rbtnFett.text = "Fett (Niedrigste zuerst)"
        }
    }

    // Sortierung Speichern:
    private fun saveSorting()
    {
        fun getAttribute():String
        {
            var export=""
            when(rbtnGroup.checkedRadioButtonId)
            {
                R.id.dialog_sorter_rbtn_standard -> export= ""
                R.id.dialog_sorter_rbtn_name -> export= "name"
                R.id.dialog_sorter_rbtn_gruppe -> export= "category"
                R.id.dialog_sorter_rbtn_kcal -> export= "kcal"
                R.id.dialog_sorter_rbtn_carbs -> export= "carb"
                R.id.dialog_sorter_rbtn_proteine -> export= "protein"
                R.id.dialog_sorter_rbtn_fett -> export= "fett"

            }
            return export

        }
        if(mListener!=null)
        {
            Log.d("Smeasy","DialogFoodListSorter - sortierung gespeichert mit SortingItem: ${getAttribute()}")
            mListener.setOnDialogClickListener(getAttribute(),aufsteigend)
            alertDialog.dismiss()
        }
    }


    // Implementierte Funktionen...
    override fun onClick(p0: View?) {
        when(p0)
        {
            btnChange -> changeTexts()
            btnAbort -> alertDialog.dismiss()
            btnSave -> saveSorting()
        }
    }

    // Interface:
    interface OnDialogClickListener
    {
        fun setOnDialogClickListener(name:String,aufsteigend:Boolean)
    }

    fun setOnDialogClickListener(mListener: OnDialogClickListener)
    {
        this.mListener = mListener
    }
}
