package de.rohnert.smarteatingsystem.ui.foodtracker.fragment.foodchooser

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smarteatingsystem.ui.foodtracker.adapter.ClassicFoodListAdapter
import de.rohnert.smarteatingsystem.ui.foodtracker.dialogs.*
import de.rohnert.smarteatingsystem.ui.foodtracker.viewmodel.FoodViewModel
import de.rohnert.smarteatingsystem.ui.foodtracker.viewmodel.TAG
import kotlinx.android.synthetic.main.app_bar_main.*

class FoodListFragment: Fragment(), View.OnClickListener, SearchView.OnQueryTextListener {

    // Allgemeines
    private lateinit var rootView: View

    // Chips and Buttons:
    private lateinit var chipFilter: Button
    private lateinit var chipSortieren: Button
    private lateinit var btnAddFood: FloatingActionButton

    // RecyclerView:
    private lateinit var rv: RecyclerView
    private lateinit var adapter: ClassicFoodListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    // Toolbar:
    private lateinit var toolbar: Toolbar
    private lateinit var searchItem:MenuItem
    private lateinit var searchView:SearchView
    private var searchQuery = ""  // Speichert Suchanfragen zwischen:

    // ViewModel:
    private lateinit var foodViewModel: FoodViewModel
    private var fragmentIsVisible:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_foodlist,container,false)

        foodViewModel = ViewModelProvider(requireActivity()).get(FoodViewModel::class.java)


        initButtons()
        initRecyclerView()
        initToolbar()
        initObserver()




        return rootView
    }

    private fun initObserver()
    {
        foodViewModel.getFoodListLoaded().observe(viewLifecycleOwner, Observer {
            adapter.updateContent(foodViewModel.getFilterFoodList(),foodViewModel.getFavFoodList())
        })
    }

    private fun initButtons()
    {
        btnAddFood = rootView.findViewById(R.id.fragment_foodchooser_btn_add_food)
        chipFilter = rootView.findViewById(R.id.fragment_foodchooser_btn_filter)
        chipSortieren = rootView.findViewById(R.id.fragment_foodchooser_btn_sort)

        // Add Listener to All:
        btnAddFood.setOnClickListener(this)
        chipFilter.setOnClickListener(this)
        chipSortieren.setOnClickListener(this)
    }

    private fun initRecyclerView()
    {
        rv = rootView.findViewById(R.id.fragment_foodchooser_rv)
        adapter = ClassicFoodListAdapter(ArrayList(), ArrayList(),requireContext())
        layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        rv.adapter = adapter
        rv.layoutManager = layoutManager
        rv.setHasFixedSize(true)

        // Item Decoration:
        rv.addItemDecoration(DividerItemDecoration(rv.context,layoutManager.orientation))
        /*rv.addItemDecoration(
            CustomDividerItemDecoration(RecyclerView.VERTICAL, rootView.context, 56)
        )*/

        // Listener:
        adapter.setOnClickListener(object : ClassicFoodListAdapter.OnClickListener {
            override fun setOnClickListener(food: ExtendedFood, position: Int) {
                FoodPickerDialog(foodViewModel, rootView.context, food, foodViewModel.sMeal)

            }

        })

        adapter.setOnCheckedChangeListener(object: ClassicFoodListAdapter.OnCheckedChangedListener{
            override fun setOnCheckedChangeListener(food: ExtendedFood, buttonState:Boolean) {
                Log.d("Smeasy","DialogFragmentFoodList - initRecyclerView onCheckedChangeListener - buttonState = $buttonState")
                if(buttonState)
                {
                    foodViewModel.addNewFavFood(food.id,food.name)
                    //filter.setNewFavFoodList()
                    // TODO("Filter einstellungen!!!")
                }
                else
                {

                    foodViewModel.deleteFavFood(food.id,food.name)
                    //filter.setNewFavFoodList()
                    // TODO("Filter einstellungen!!!")
                }

            }

        })

        adapter.setOnSimpleClickListener(object:ClassicFoodListAdapter.OnSimpleClickListener{
            override fun setOnSimpleClickListener(food: ExtendedFood, position: Int) {
                foodViewModel.addNewMealEntry(food.id,100f,foodViewModel.sMeal, food.kcal, food.carb, food.protein, food.fett)
                Toast.makeText(requireContext(),"100g von ${food.name} wurden hinzugefügt",Toast.LENGTH_SHORT).show()
            }

        })


    }

    private fun initToolbar()
    {
        toolbar = requireActivity().toolbar
        fragmentIsVisible = true
        searchItem = toolbar.menu.findItem(R.id.action_foodchooser_searchview)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)




    }



    /////////////////////////////////////////////////////////////////
    // Auf diverse Buttons Klicks reagieren:
    override fun onClick(v: View?) {
        if(v == btnAddFood)
        {
            val dialog = FoodCreatorDialog(foodViewModel,requireContext())

        }
        else if(v == chipFilter)
        {
            // Start Filter:
            val dialog = DialogFoodListFilter(requireContext(),foodViewModel)
            dialog.onDialogFilterClickListener(object :DialogFoodListFilter.OnDialogFilterClickListener{
                override fun onDialogFilterClickListener() {
                    foodViewModel.searchInFoodList(searchQuery)
                }

            })

        }
        else if(v == chipSortieren)
        {
            val dialog = DialogFoodListSorter(requireContext())
            dialog.setOnDialogClickListener(object:DialogFoodListSorter.OnDialogClickListener{
                override fun setOnDialogClickListener(name: String, aufsteigend: Boolean) {
                    if(name == "")
                    {
                        foodViewModel.searchInFoodList(searchQuery)
                    }
                    else
                    {
                        foodViewModel.sortFoodList(name,aufsteigend)
                    }

                }

            })

        }
    }


    // Searching Foods:
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(fragmentIsVisible)
        {
            Log.d(TAG,"FoodListFragment onQueryTextSubmit with query: $query")
            searchQuery = query ?: ""
            foodViewModel.searchInFoodList(searchQuery)
        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(fragmentIsVisible)
        {
            Log.d(TAG,"FoodListFragment onQueryTextChange with newText: $newText")
            searchQuery = newText ?: ""
            foodViewModel.searchInFoodList(searchQuery)
        }


        return true
    }


    override fun onPause() {
        fragmentIsVisible = false
        searchView.setQuery("", false)

        Log.d(TAG,"FoodListFragment onPause() - fragmentIsVisible = $fragmentIsVisible")
        super.onPause()
    }

    override fun onResume() {
        if(!fragmentIsVisible)
        {
            searchView.setOnQueryTextListener(this)
            searchView.setQuery(searchQuery, false)
            fragmentIsVisible = true
        }


        Log.d(TAG,"FoodListFragment onResume() - fragmentIsVisible = $fragmentIsVisible")
        super.onResume()
    }

    override fun onDestroy() {
        Log.d(TAG,"FoodListFragment onDestroy() - fragmentIsVisible = $fragmentIsVisible")
        foodViewModel.searchInFoodList("")
        super.onDestroy()
    }




}