package de.rohnert.smarteatingsystem.frontend.foodtracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

import de.rohnert.smarteatingsystem.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel.FoodViewModel

class DialogFoodListFilter(var context: Context, var foodViewModel: FoodViewModel) : View.OnClickListener {


    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater

    private var allCategories:ArrayList<String> = foodViewModel.getFoodCategories()
    private var prefs = SharedAppPreferences(context)
    // Interface Stuff:
    lateinit var mListener: OnDialogFilterClickListener

    // Status Stuff:
    private var categories:ArrayList<String> = foodViewModel.filterCategory
    private var allowedFood:Boolean = foodViewModel.onlyAllowedFoodFilter
    private var favourite:Boolean = foodViewModel.onlyFavouriteFood
    private var userFood:Boolean = foodViewModel.onlyUserFoodFilter

    // Extended Filter:
    //private var extendFilterValues:ArrayList<Float> = arrayListOf(0f,0f,0f,0f,0f,0f,0f,0f)

    // View Elemente:
    // TextViews:
    private lateinit var tvCategories: TextView
    // Buttons:
    private lateinit var btnCategory:FrameLayout
    private lateinit var btnReset:Button
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button
    // Switch Buttons:

    private lateinit var switchFavourites:Switch
    private lateinit var switchUserFood:Switch
    // ChipGroup
    private lateinit var chipGroup: ChipGroup
    private lateinit var chips:ArrayList<Chip>

    // Premiumfunktionen:
    private lateinit var tvAllowedFoodTitle:TextView
    private lateinit var tvAdvancedFilterTitle:TextView
    private lateinit var tvAdvancedFilterValue:TextView
    private lateinit var btnAdvancedFilter:ImageButton
    private lateinit var btnAllowedFoodTitle:ImageButton
    private lateinit var switchAllowed:Switch




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

        //initChipGroupContent()



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
        /*btnExtras = view.findViewById(R.id.dialog_filter_btn_extra)*/

        // InitSwitches:
        switchFavourites = view.findViewById(R.id.dialog_filter_switch_favourites)
        switchUserFood = view.findViewById(R.id.dialog_filter_switch_userfood)
        switchAllowed = view.findViewById(R.id.dialog_filter_switch_allowedfood)
        // Aktiv Switches
        switchUserFood.isChecked = userFood
        switchFavourites.isChecked = favourite
        switchAllowed.isChecked = allowedFood



        //FrameLayout as Button
        btnCategory = view.findViewById(R.id.dialog_filter_btn_category)


        // TextViews...
        tvCategories = view.findViewById(R.id.dialog_filter_category_elements)
        tvCategories.text = getCategoryElements()


        // Listener installieren:
        btnSave.setOnClickListener(this)
        btnReset.setOnClickListener(this)
        btnAbort.setOnClickListener(this)
        btnCategory.setOnClickListener(this)
        //btnExtendFilter.setOnClickListener(this)
        /*btnExtras.setOnClickListener(this)
       */

    }




    // ClickListener....
    override fun onClick(p0: View?) {
        when(p0)
        {
            btnSave -> startSaveProcess(true)
            btnReset -> startResetProcess()
            btnAbort -> alertDialog.dismiss()
            btnCategory ->
            {
                var dialog = DialogFoodFilterCategories(context,allCategories,categories)
                dialog.setOnDialogFoodCategoryListener(object: DialogFoodFilterCategories.OnDialogFoodCategoryListener{
                    override fun setOnDialogFoodCategoryListener(values: ArrayList<String>) {
                        categories = values
                        tvCategories.text = getCategoryElements()
                    }

                })
            }

        }
    }

    private fun getCategoryElements():String
    {
        var export = ""
        for((index,i) in categories.withIndex())
        {
            if(index == 0)
            {
                export += i
            }
            else if (index < 4)
            {
                export += " - $i"
            }
            else
            {
                export += " - ..."
                break
            }
        }
        if(export == "")
        {
            export = " - "
        }
        return export

    }

    // Methode, wenn Speicher Button angeklickt wird
    private fun startSaveProcess(exit:Boolean) {



        foodViewModel.onlyAllowedFoodFilter = switchAllowed.isChecked
        foodViewModel.onlyFavouriteFood = switchFavourites.isChecked
        foodViewModel.onlyUserFoodFilter = switchUserFood.isChecked
        foodViewModel.filterCategory = categories
        mListener?.onDialogFilterClickListener()



        if(exit)alertDialog.dismiss()
    }

    // Methode, wenn auf Reset geklickt wird...
    private fun startResetProcess()
    {
        //
        switchAllowed.isChecked = false
        switchFavourites.isChecked = false
        switchUserFood.isChecked = false
        categories = foodViewModel.getFoodCategories()
        tvCategories.text = getCategoryElements()
        startSaveProcess(false)

    }




    interface OnDialogFilterClickListener
    {
        fun onDialogFilterClickListener()
    }

    fun onDialogFilterClickListener(mListener: OnDialogFilterClickListener)
    {
        this.mListener = mListener
    }


}