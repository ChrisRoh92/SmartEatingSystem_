package de.rohnert.smarteatingsystem.frontend.foodtracker.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.frontend.foodtracker.adapter.CategoryFilterAdapter
import androidx.recyclerview.widget.DividerItemDecoration




class DialogFoodFilterCategories(var context: Context,var categories:ArrayList<String>, var activCategories: ArrayList<String>) :
    View.OnClickListener, CompoundButton.OnCheckedChangeListener {



    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var view: View
    private lateinit var inflater: LayoutInflater

    // Interface
    private lateinit var mListener:OnDialogFoodCategoryListener

    // RecyclerView:
    private lateinit var recyclerview:RecyclerView
    private lateinit var manager:LinearLayoutManager
    private lateinit var adapter: CategoryFilterAdapter

    // Button
    private lateinit var btnSave:Button
    private lateinit var btnAbort:Button
    // CheckBox:
    private lateinit var cbAll:CheckBox


    init {
        initDialog()
    }


    // Dialog initialisieren...
    private fun initDialog()
    {

        builder = AlertDialog.Builder(context)
        inflater = LayoutInflater.from(context)
        view = inflater.inflate(R.layout.dialog_choose_category, null)
        builder.setView(view)


        initViews()
        initRecyclerView()
        //initChipGroupContent()



        alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        alertDialog.show()

    }

    private fun initViews()
    {
        // Buttons initialisieren:
        btnSave = view.findViewById(R.id.dialog_choose_category_btn_save)
        btnAbort = view.findViewById(R.id.dialog_choose_category_btn_abort)

        // CheckBox:
        cbAll = view.findViewById(R.id.dialog_choose_category_cb_all)

        // Listener installieren:
        btnSave.setOnClickListener(this)
        btnAbort.setOnClickListener(this)

        cbAll.setOnCheckedChangeListener(this)

    }

    private fun initRecyclerView()
    {

        recyclerview = view.findViewById(R.id.dialog_choose_category_rv)
        manager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        // ActivContent ArrayList erstellen:
        var activ:ArrayList<Boolean> = ArrayList()
        for(i in categories)
        {
            var vorhanden = false
            for(j in activCategories)
            {
                if(i == j)
                {
                    activ.add(true)
                    vorhanden = true
                }

            }
            if(!vorhanden)
            {
                activ.add(false)
            }


        }
        adapter = CategoryFilterAdapter(categories,activ)
        recyclerview.layoutManager = manager
        recyclerview.adapter = adapter

        recyclerview.addItemDecoration(DividerItemDecoration(
            recyclerview.context,
            manager.orientation
        ))
    }


    private fun getChoosenCategories():ArrayList<String>
    {
        var values:ArrayList<String> = ArrayList()
        var booleanList = adapter.activContent

        for((index,i) in booleanList.withIndex())
        {
            if(i)
            {
                values.add(categories[index])
            }
        }


        return values
    }



    // Implementierte Methoden:
    override fun onClick(v: View?)
    {
        if(v == btnAbort)
        {
            alertDialog.dismiss()
        }

        else
        {
            if(mListener!=null)
            {

                mListener.setOnDialogFoodCategoryListener(getChoosenCategories())
                alertDialog.dismiss()
            }
        }

    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        adapter.setAllContentToState(isChecked)
    }



    // Interface einrichten:
    interface OnDialogFoodCategoryListener
    {
        fun setOnDialogFoodCategoryListener(values:ArrayList<String>)
    }

    fun setOnDialogFoodCategoryListener(mListener:OnDialogFoodCategoryListener)
    {
        this.mListener = mListener
    }
}