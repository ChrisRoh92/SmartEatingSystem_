package de.rohnert.smeasy.frontend.foodtracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.core.view.ViewCompat

import de.rohnert.smeasy.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import de.rohnert.smeasy.frontend.foodtracker.FoodViewModel2

class DialogFoodListFilter(var context: Context, var foodViewModel: FoodViewModel2, var categories:ArrayList<String>) : View.OnClickListener {


    lateinit var builder: AlertDialog.Builder
    lateinit var alertDialog: AlertDialog
    lateinit var view: View
    lateinit var inflater: LayoutInflater
    var allCategories:ArrayList<String> = foodViewModel.getFoodCategories()
    // Interface Stuff:
    lateinit var mListener: OnDialogFilterClickListener

    // View Elemente:
    // Buttons:
    private lateinit var btnReset:Button
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button
    private lateinit var btnExtras:Button
    // Switch Buttons:
    private lateinit var switchAllowed:Switch
    private lateinit var switchFavourites:Switch
    private lateinit var switchUserFood:Switch
    // ChipGroup
    private lateinit var chipGroup: ChipGroup
    private lateinit var chips:ArrayList<Chip>




    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_foodlist_filter, null)
        builder.setView(view)


        initViews()
        initChipGroupContent()



        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    fun initViews()
    {
        // Init Buttons:
        btnSave = view.findViewById(R.id.dialog_filter_btn_save)
        btnReset = view.findViewById(R.id.dialog_filter_btn_reset)
        btnAbort = view.findViewById(R.id.dialog_filter_btn_abort)
        btnExtras = view.findViewById(R.id.dialog_filter_btn_extra)

        // InitSwitches:
        switchAllowed = view.findViewById(R.id.dialog_filter_switch_allowedfood)
        switchFavourites = view.findViewById(R.id.dialog_filter_switch_favourites)
        switchUserFood = view.findViewById(R.id.dialog_filter_switch_userfood)

        // Init Chip Group
        chipGroup = view.findViewById(R.id.dialog_filter_chip_group)

        // Listener installieren:
        btnSave.setOnClickListener(this)
        btnReset.setOnClickListener(this)
        btnAbort.setOnClickListener(this)
        btnExtras.setOnClickListener(this)

    }

    fun initChipGroupContent()
    {

        chips = ArrayList()
        for(i in allCategories)
        {
            var chip: Chip = inflater.inflate(R.layout.chip_item,null,false) as Chip
            chip.text = "$i"
            chip.id = ViewCompat.generateViewId()
            chip.isChecked = categories.contains("$i")
            chips.add(chip)
            chipGroup.addView(chip)
        }


    }


    // ClickListener....
    override fun onClick(p0: View?) {
        when(p0)
        {
            btnSave -> startSaveProcess(true)
            btnReset -> startResetProcess()
            btnAbort -> alertDialog.dismiss()
            btnExtras -> Toast.makeText(view.context,"Das ist eine Premium Funktion",Toast.LENGTH_SHORT).show()
        }
    }


    // Methode, wenn Speicher Button angeklickt wird
    fun startSaveProcess(exit:Boolean) {
        var export: ArrayList<String> = ArrayList()
        for (i in chips) {
            if (i.isChecked) export.add(i.text.toString())
        }
        if(mListener!=null)
        {
            mListener.onDialogFilterClickListener(
                export,
                switchAllowed.isChecked,
                switchFavourites.isChecked,
                switchUserFood.isChecked
            )
        }

        if(exit)alertDialog.dismiss()
    }

    // Methode, wenn auf Reset geklickt wird...
    fun startResetProcess()
    {
        for(i in chips) i.isChecked = true
        switchAllowed.isChecked = false
        switchFavourites.isChecked = false
        switchUserFood.isChecked = false
        startSaveProcess(false)

    }




    interface OnDialogFilterClickListener
    {
        fun onDialogFilterClickListener(category:ArrayList<String>, allowedFood:Boolean, favouriten:Boolean,userFood:Boolean)
    }

    fun onDialogFilterClickListener(mListener: OnDialogFilterClickListener)
    {
        this.mListener = mListener
    }


}