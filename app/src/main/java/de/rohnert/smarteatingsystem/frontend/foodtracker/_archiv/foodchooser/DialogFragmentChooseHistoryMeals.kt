package de.rohnert.smarteatingsystem.frontend.foodtracker._archiv.foodchooser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.backend.databases.daily_database.helper.CalcedFood
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel.FoodViewModel

class DialogFragmentChooseHistoryMeals(var foodViewModel: FoodViewModel, var meal:String): DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var rootView: View
    private var helper = Helper()
    private var mDate:String = foodViewModel.date

    // RecyclerView
    private lateinit var rv:RecyclerView
    private lateinit var adapter:HistoricFoodChooserAdapter
    private lateinit var layoutManager:LinearLayoutManager

    // Content:
    private var titleList:ArrayList<String> = ArrayList()
    private var contentList:ArrayList<ArrayList<ArrayList<CalcedFood>>> = ArrayList()

    // View Element:
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialog
        )


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        rootView = inflater.inflate(R.layout.dialog_choose_history_meals, container, false)


        createContent()
        initToolBar()



        // dialog_historymeals_toolbar

        return rootView
    }

    private fun initToolBar() {
        toolbar = rootView.findViewById(R.id.dialog_historymeals_toolbar)
        //toolbar.collapseIcon = ContextCompat.getDrawable(rootView.context,)
        toolbar.setNavigationOnClickListener {
            // handle back button naviagtion
            dismiss()
        }
        toolbar.inflateMenu(R.menu.menu_historic_chooser)
        toolbar.setOnMenuItemClickListener{
            saveEntries()
            true
        }


    }

    private fun createContent()
    {
        foodViewModel.getAllCalcedFood().observe(viewLifecycleOwner,androidx.lifecycle.Observer
        {
            for(i in foodViewModel.getLocalDailyList())
            {
                if(i.date != helper.getStringFromDate(helper.getCurrentDate()))
                {
                    titleList.add(i.date)
                }

            }
            contentList = foodViewModel.getAllCalcedFoodList()
            createRecyclerView()
        })
        foodViewModel.getAllCalcedFoods()
    }

    private fun createRecyclerView()
    {
        rv = rootView.findViewById(R.id.dialog_historymeals_rv)
        layoutManager = LinearLayoutManager(rootView.context,RecyclerView.VERTICAL,false)
        adapter = HistoricFoodChooserAdapter(contentList,titleList)
        rv.layoutManager = layoutManager
        rv.adapter = adapter
        layoutManager.scrollToPositionWithOffset(titleList.lastIndex,0)


    }

    private fun saveEntries()
    {
        if(rv!=null && adapter != null)
        {
            // Daten abrufen:
            var data = adapter.getCalcedFoods()
            if(data.isNotEmpty())
            {
                foodViewModel.addNewMealEntries(data,meal)
            }

            dismiss()


        }
    }
}