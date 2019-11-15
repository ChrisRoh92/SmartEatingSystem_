package de.rohnert.smeasy.frontend.foodtracker.dialogs

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import de.rohnert.smeasy.R
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.frontend.foodtracker.FoodViewModel2
import de.rohnert.smeasy.frontend.foodtracker.adapter.ClassicFoodListAdapter
import de.rohnert.smeasy.frontend.foodtracker.adapter.FoodListAdapter
import de.rohnert.smeasy.frontend.foodtracker.helper.FoodListerFilter
import de.rohnert.smeasy.helper.others.CustomDividerItemDecoration
import de.rohnert.smeasy.moduls.foodtracker.dialogs.DialogFoodListSorter


class DialogFragmentFoodList(var sMeal:String,var foodViewModel: FoodViewModel2): DialogFragment(), Toolbar.OnMenuItemClickListener, SearchView.OnQueryTextListener {


    // ,
    //    FoodListRecyclerAdapter.onCustomItemClickListener,
    //    DialogFoodListFilter.OnDialogFilterClickListener

    // Allgemeine Variablen:
    private lateinit var rootView: View
    private lateinit var sharePrefs:SharedAppPreferences

    // Content:
    private lateinit var filter:FoodListerFilter
    private var foodList:ArrayList<Food> = foodViewModel.getLocalFoodList()
    private var workFoodList:ArrayList<Food> = foodList


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

        filter = FoodListerFilter(rootView.context,foodViewModel)
        filter.setOnFilterItemsListener(object:FoodListerFilter.OnFilterItemsListener{
            override fun setOnFilterItemsListener(foodList: ArrayList<Food>) {
                classicAdapter.updateContent(foodList)

                Log.d("Smeasy","DialogFragmentFoodList - initRecyclerView - setOnFilterItemsListener was called: Size of foodList: ${foodList.size}")
            }

        })

        rv = rootView.findViewById(R.id.dialog_foodlist_rv)
        manager = LinearLayoutManager(rootView.context, RecyclerView.VERTICAL, false)
        adapter = FoodListAdapter(sMeal)
        classicAdapter = ClassicFoodListAdapter(filter.getFilteredFoodList(),rootView.context)
        rv.layoutManager = manager
        rv.adapter = classicAdapter




        /*adapter.submitList(foodViewModel.getFoodList().value!!)*//*
        //var animator = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        foodViewModel.getFoodList().observe(viewLifecycleOwner, Observer {

            //classicAdapter.updateContent(foodViewModel.getFoodList().value!!)
            *//*adapter.submitList(foodViewModel.getFoodList().value!!)
            Handler().postDelayed({
                pb.visibility = View.GONE
                Thread.sleep(100)
                rv.visibility = View.VISIBLE
            },500)*//*

        })*/



        rv.addItemDecoration(
            CustomDividerItemDecoration(RecyclerView.VERTICAL, rootView.context, 40)
        )

        classicAdapter.setOnClickListener(object : ClassicFoodListAdapter.OnClickListener {
            override fun setOnClickListener(food: Food, position: Int) {
                var dialog = FoodPickerDialog(foodViewModel, rootView.context, food, sMeal)
            }

        })

        adapter.setOnLongClickListener(object : FoodListAdapter.OnLongClickListener {
            override fun setOnLongClickListener(food: Food, position: Int, meal: String) {

            }

        })


    }

    // Bei Klick auf den Button wird ein neues Food erstellt...
    fun initFoodCreator() {
        btnAdd = rootView.findViewById(R.id.dialog_foodlist_btn)
        btnAdd.setOnClickListener {
            //var dialog = FoodCreatorDialog(foodViewModel,rootView.context)


        }
    }

    fun initToolBar() {
        toolbar = rootView.findViewById(R.id.dialog_foodlist_toolbar)
        //toolbar.collapseIcon = ContextCompat.getDrawable(rootView.context,)
        toolbar.setNavigationOnClickListener {
            // handle back button naviagtion
            dismiss()
        }
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

            var handler = Handler()
            handler.postDelayed(Runnable {
                var filterDialog =
                    DialogFoodListFilter(rootView.context, foodViewModel, categories)
                filterDialog.onDialogFilterClickListener(object :
                    DialogFoodListFilter.OnDialogFilterClickListener {
                    override fun onDialogFilterClickListener(category: ArrayList<String>,allowedFood: Boolean,favouriten: Boolean,userFood: Boolean)
                    {
                        Log.d("Smeasy","DialogFragmentFoodList - onDialogFilterClickListener() - Categories: $category")
                        filter.setCategories(category)
                        Log.d("Smeasy","DialogFragmentFoodList - onMenuItemClick - onDialogFilterClickListener was called: Size of categories: ${foodList.size}")
                        categories = category
                        //filterFoods()
                        /*categories = category
                        pb.visibility = View.VISIBLE
                        rv.visibility = View.GONE
                        var handler = Handler()
                        handler.post {
                            classicAdapter.updateContent(foodViewModel.getFoodListFiltered(categories))
                            pb.visibility = View.GONE
                            rv.visibility = View.VISIBLE
                            //adapter.submitList(foodViewModel.getFoodList().value!!)
                            //adapter.submitList(foodViewModel.searchInAppFoodList(sItem, categories))
                        }*/
                    }

                })
            }, 50)


        } else if (item!!.itemId == R.id.foodlist_sort) {

            var handler = Handler()
            handler.postDelayed(Runnable {
                var sortDialog = DialogFoodListSorter(rootView.context,filter)
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
        searchInFoodList(query!!)



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


    override fun onPause() {
        super.onPause()
        //foodViewModel.createFoodList()
    }


    private fun filterFoods()
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

            workFoodList = values
            classicAdapter.updateContent(workFoodList)
        }
        else
        {
            workFoodList = foodList
            classicAdapter.updateContent(workFoodList)
        }

    }

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
        classicAdapter.updateContent(filter.filterByItemSearch(item))

    }

}