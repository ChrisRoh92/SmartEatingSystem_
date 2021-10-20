package de.rohnert.smarteatingsystem.ui.foodtracker.dialogs

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.data.databases.food_database.normal_database.Food
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smarteatingsystem.data.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.ui.foodtracker.viewmodel.FoodViewModel
import de.rohnert.smarteatingsystem.ui.foodtracker.adapter.ClassicFoodListAdapter
import de.rohnert.smarteatingsystem.ui.foodtracker.adapter.FoodListAdapter
import de.rohnert.smarteatingsystem.ui.foodtracker.helper.FoodListerFilter
import de.rohnert.smarteatingsystem.utils.others.CustomDividerItemDecoration


class DialogFragmentFoodList(var sMeal:String,var foodViewModel: FoodViewModel): DialogFragment(), Toolbar.OnMenuItemClickListener, SearchView.OnQueryTextListener {


    // ,
    //    FoodListRecyclerAdapter.onCustomItemClickListener,
    //    DialogFoodListFilter.OnDialogFilterClickListener

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var sharePrefs:SharedAppPreferences

    // Content:
    private lateinit var filter:FoodListerFilter
    private var favFoodList = foodViewModel.getFavFoodList()
    private var foodList:ArrayList<ExtendedFood> = foodViewModel.getLocalFoodList()
    private var workFoodList:ArrayList<ExtendedFood> = foodList
    private var extendFilterValues:ArrayList<Float> = arrayListOf(0f,0f,0f,0f,0f,0f,0f,0f)
    private var favouriteStatus = false
    private var userFoodStatus = false
    private var allowedFoodStatus = false


    // Filter stuff:
    private var sItem = ""
    private var categories: ArrayList<String> = foodViewModel.getFoodCategories()
    private var maxCategory = categories.size
    private lateinit var pb: ProgressBar


    // Toolbar:
    private lateinit var toolbar: Toolbar
    private lateinit var searchItem: MenuItem
    private lateinit var searchView: SearchView

    // View Elemente:
    private lateinit var rv: RecyclerView
    private lateinit var manager: LinearLayoutManager
    private lateinit var adapter: FoodListAdapter
    private lateinit var classicAdapter: ClassicFoodListAdapter

    // Food Creator:
    private lateinit var btnAdd: Button


    ///////////////////////////////////////////////////////////////////////////////////////////////


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

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View?
    {
        Log.d("Smeasy", "DialogFragmentFoodList - onCreateView")
        rootView = inflater.inflate(R.layout.dialog_foodlist, container, false)

        sharePrefs = SharedAppPreferences(rootView.context)
        if(sharePrefs.maxAllowedKcal == 0f)
        {
            sharePrefs.setNewMaxAllowedKcal(200f)
        }

        // ProgressBar:
        pb = rootView.findViewById(R.id.dialog_foodlist_pb)
        if (pb.visibility != View.INVISIBLE) pb.visibility = View.INVISIBLE

        initToolBar()
        initRecyclerView()
        initFoodCreator()




        return rootView
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Init Views....
    private fun initRecyclerView() {

        filter = FoodListerFilter(rootView.context,foodViewModel,extendFilterValues)
        filter.setOnFilterItemsListener(object:FoodListerFilter.OnFilterItemsListener{
            override fun setOnFilterItemsListener(foodList: ArrayList<ExtendedFood>) {
                //classicAdapter.updateContent(foodList)

                Log.d("Smeasy","DialogFragmentFoodList - initRecyclerView - setOnFilterItemsListener was called: Size of foodList: ${foodList.size}")
            }

        })

        rv = rootView.findViewById(R.id.dialog_foodlist_rv)
        manager = LinearLayoutManager(rootView.context, RecyclerView.VERTICAL, false)
        adapter = FoodListAdapter(sMeal)
        classicAdapter = ClassicFoodListAdapter(filter.getFilteredFoodList(),favFoodList,rootView.context)
        rv.layoutManager = manager
        rv.adapter = classicAdapter






        rv.addItemDecoration(
            CustomDividerItemDecoration(RecyclerView.VERTICAL, rootView.context, 56)
        )

        classicAdapter.setOnClickListener(object : ClassicFoodListAdapter.OnClickListener {
            override fun setOnClickListener(food: ExtendedFood, position: Int) {
                var dialog = FoodPickerDialog(foodViewModel, rootView.context, food, sMeal)
            }

        })

        adapter.setOnLongClickListener(object : FoodListAdapter.OnLongClickListener {
            override fun setOnLongClickListener(food: Food, position: Int, meal: String) {

            }

        })

        // Favouriten setzen bzw. entfernen...
        classicAdapter.setOnCheckedChangeListener(object: ClassicFoodListAdapter.OnCheckedChangedListener{
            override fun setOnCheckedChangeListener(food: ExtendedFood, buttonState:Boolean) {
                Log.d("Smeasy","DialogFragmentFoodList - initRecyclerView onCheckedChangeListener - buttonState = $buttonState")
                if(buttonState)
               {
                   foodViewModel.addNewFavFood(food.id,food.name)
                   filter.setNewFavFoodList()
               }
                else
               {

                   foodViewModel.deleteFavFood(food.id,food.name)
                   filter.setNewFavFoodList()
               }

            }

        })

        // Observer für die LiveDaten...
        foodViewModel.getUpdatedFoodValue().observe(viewLifecycleOwner, Observer{
            categories = foodViewModel.getFoodCategories()
            filter.setNewFoodList()
        })


    }

    // Bei Klick auf den Button wird ein neues Food erstellt...
    fun initFoodCreator() {
        btnAdd = rootView.findViewById(R.id.dialog_foodlist_btn)
        btnAdd.setOnClickListener {
            var dialog = FoodCreatorDialog(foodViewModel,rootView.context)
            //Toast.makeText(rootView.context,"Funktion befindet sich noch im Aufbau...", Toast.LENGTH_SHORT).show()


        }
    }

    fun initToolBar() {
        toolbar = rootView.findViewById(R.id.dialog_foodlist_toolbar)
        //toolbar.collapseIcon = ContextCompat.getDrawable(rootView.context,)
        toolbar.setNavigationOnClickListener {
            // handle back button naviagtion
            dismiss()
        }
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.foodlist_menu)

        toolbar.setOnMenuItemClickListener(this)

        searchItem = toolbar.menu.findItem(R.id.action_foodlist_search)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Implementierte Funktionen...


    // Bitte hier korrigieren!!!
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        Log.d("Smeasy", "DialogFragmentFoodList - onOptionsItemSelected(item: MenuItem):Boolean")
        if (item!!.itemId == R.id.foodlist_filter) {




        } else if (item.itemId == R.id.foodlist_sort) {

            var handler = Handler()
            handler.postDelayed({
                var sortDialog = DialogFoodListSorter(rootView.context)
                sortDialog.setOnDialogClickListener(object: DialogFoodListSorter.OnDialogClickListener
                {
                    override fun setOnDialogClickListener(name: String, aufsteigend: Boolean) {
                        filter.setSortItem(name,aufsteigend)
                        /*when(name)
                        {
                            "name" -> adapter.updateContent(foodViewModel.filter.getFoodListSortedByName(adapter.content,aufsteigend))
                            "gruppe" -> adapter.updateContent(foodViewModel.filter.getFoodListSortedByCategory(adapter.content,aufsteigend))
                            "kcal" -> adapter.updateContent(foodViewModel.filter.getFoodListSortedByKcal(adapter.content,aufsteigend))
                            "carbs" -> adapter.updateContent(foodViewModel.filter.getFoodListSortedByCarb(adapter.content,aufsteigend))
                            "protein" -> adapter.updateContent(foodViewModel.filter.getFoodListSortedByProtein(adapter.content,aufsteigend))
                            "fett" -> adapter.updateContent(foodViewModel.filter.getFoodListSortedByFett(adapter.content,aufsteigend))
                        }*/
                    }

                })
            },50)

        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("Smeasy", "DialogFragmentFoodList - onQueryTextSubmit text: $query")
        sItem = query!!
        /*pb.visibility = View.VISIBLE
        rv.visibility = View.GONE
        var handler = Handler()
        handler.post {
            foodViewModel.searchInAppFoodList(sItem, categories)
        }*/
        searchInFoodList(query)



        //adapter.updateContent(foodViewModel.getFilteredFoodList(sItem))
        //adapter.updateContent(foodViewModel.filter.getFilteredFoodList(sItem,adapter.content))

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (TextUtils.isEmpty(newText)) {
            Log.d("Smeasy", "DialogFragmentFoodList - onQueryTextChange text: All Deleted...")
            /*pb.visibility = View.VISIBLE
            rv.visibility = View.GONE
            foodViewModel.createFoodList()
            sItem = ""*/
            searchInFoodList(newText!!)

        } else {
            searchInFoodList(newText!!)


        }

        return true
    }

    /*override fun onDialogFilterClickListener(category: ArrayList<String>,allowedFood: Boolean,favouriten: Boolean,userFood: Boolean)
    {
        categories = category
        adapter.updateContent(foodViewModel.filter.getFoodsByCategory(categories))

    }*/
    ///////////////////////////////////////////////////////////////////////////////////////////////


    /*private fun getFilteredFoodList():ArrayList<Food>
    {
        if(categories.size != maxCategory)
        {
            var values:ArrayList<Food> = ArrayList()
            for(i in workFoodList)
            {
                for(j in categories)
                {
                    if(j == i.category)
                    {
                        values.add(i)
                        continue
                    }
                }
            }

            return values
        }
        else
        {
            workFoodList = foodList
            return workFoodList
        }
    }*/

    private fun searchInFoodList(item:String)
    {
        /*if(item != "")
        {
            var values:ArrayList<Food> = ArrayList()
            for(i in getFilteredFoodList())
            {
                if(i.name.toLowerCase().contains(item.toLowerCase()) || i.category.toLowerCase().contains(item.toLowerCase()) )
                {
                    values.add(i)
                }
            }
            workFoodList = values
            classicAdapter.updateContent(workFoodList)
        }
        else
        {
            workFoodList = getFilteredFoodList()
            classicAdapter.updateContent(workFoodList)
        }*/
        //classicAdapter.updateContent(filter.filterByItemSearch(item))

    }

}