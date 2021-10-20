package de.rohnert.smarteatingsystem.ui.foodtracker.fragment.foodchooser

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.rohnert.smarteatingsystem.R
import de.rohnert.smarteatingsystem.ui.foodtracker.adapter.MealListRecyclerViewAdapter
import de.rohnert.smarteatingsystem.ui.foodtracker.viewmodel.FoodViewModel
import de.rohnert.smarteatingsystem.ui.foodtracker.viewmodel.TAG
import kotlinx.android.synthetic.main.app_bar_main.*

class MealListFragment:Fragment(),SearchView.OnQueryTextListener
{
    private lateinit var rootView:View

    // RecyclerView:
    private lateinit var rv:RecyclerView
    private lateinit var adapter:MealListRecyclerViewAdapter
    private lateinit var layoutManager:LinearLayoutManager

    // Toolbar:
    private lateinit var toolbar: Toolbar
    private lateinit var searchItem: MenuItem
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
        rootView = inflater.inflate(R.layout.fragment_meallist,container,false)

        initToolbar()
        initRecyclerView()

        return rootView
    }

    private fun initToolbar()
    {
        toolbar = requireActivity().toolbar

        searchItem = toolbar.menu.findItem(R.id.action_foodchooser_searchview)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)

    }

    private fun initRecyclerView()
    {
        rv = rootView.findViewById(R.id.fragment_meallist_rv)
        adapter = MealListRecyclerViewAdapter()
        layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        rv.adapter = adapter
        rv.layoutManager = layoutManager
    }

    // Searching Foods:
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(fragmentIsVisible)
        {
            Log.d(TAG,"MealListFragment onQueryTextSubmit with query: $query")
            searchQuery = query ?: ""
        }

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(fragmentIsVisible)
        {
            Log.d(TAG,"MealListFragment onQueryTextChange with newText: $newText")
            searchQuery = newText ?: ""
        }

        return true
    }


    override fun onPause() {

        fragmentIsVisible = false
        searchView.setQuery("", false)


        Log.d(TAG,"MealListFragment onPause() - fragmentIsVisible = $fragmentIsVisible")
        super.onPause()
    }

    override fun onResume() {

        if(!fragmentIsVisible)
        {
            searchView.setOnQueryTextListener(this)
            searchView.setQuery(searchQuery, false);
            fragmentIsVisible = true
        }
        Log.d(TAG,"MealListFragment onResume() - fragmentIsVisible = $fragmentIsVisible")
        super.onResume()
    }
}