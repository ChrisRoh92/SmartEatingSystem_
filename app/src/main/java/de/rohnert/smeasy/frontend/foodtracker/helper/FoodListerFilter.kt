package de.rohnert.smeasy.frontend.foodtracker.helper

import android.content.Context
import android.util.Log
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.frontend.foodtracker.FoodViewModel2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FoodListerFilter(var context: Context, var foodViewModel:FoodViewModel2)
{
    // Default Operators.
    private var foodlist:ArrayList<Food> = foodViewModel.getLocalFoodList()
    private var categories:ArrayList<String> = foodViewModel.getFoodCategories()
    private var allowedFood:Boolean = false
    private var userFood:Boolean = false
    private var favourites:Boolean = false
    private var sortingItem:String = ""
    private var up:Boolean = false
    private var sharePrefs = SharedAppPreferences(context)

    // Interface
    private lateinit var mListener:OnFilterItemsListener

    init {

    }

    fun getFilteredFoodList():ArrayList<Food>
    {
        var export:ArrayList<Food> = filterListByCategories(foodlist)

        if(allowedFood) export = filterByAllowedFood(export)
        if(userFood) export = filterByUserFood(export)
        if(favourites) export = filterByFavourites(export)
        if(sortingItem!="")
        {
            Log.d("Smeasy","FoodListFilter - sortingItem ist aktiv: $sortingItem")
            var sortList:ArrayList<Food> = ArrayList()
            sortList = sortList(export)
            export = sortList
        }

        return export

    }

    fun filterByItemSearch(item:String):ArrayList<Food>
    {
        if(item != "")
        {

            var export:ArrayList<Food> = ArrayList()
            for(i in getFilteredFoodList())
            {
                if(i.name.toLowerCase().contains(item.toLowerCase()) || i.category.toLowerCase().contains(item.toLowerCase()) )
                {
                    export.add(i)
                }
            }
            if(sortingItem == "")
                return export
            else
                return sortList(export)
        }
        else
        {
            if(sortingItem == "")
                return getFilteredFoodList()
            else
                return sortList(getFilteredFoodList())

        }


    }

    private fun sortList(list:ArrayList<Food>):ArrayList<Food>
    {
        var export:List<Food>? = null
        when(sortingItem)
        {
            //"name" -> export = ArrayList(list.sortedWith(compareBy { it.name }))
            "name" ->
            {
                export = list.sortedWith(compareBy { it.name })
            }
            "category" -> export = list.sortedWith(compareBy { it.category })
            "kcal" -> export = list.sortedWith(compareBy { it.kcal })
            "carb" -> export = list.sortedWith(compareBy { it.carb })
            "protein" -> export = list.sortedWith(compareBy { it.protein })
            "fett" -> export = list.sortedWith(compareBy { it.fett })
        }
        Log.d("Smeasy","FoodListFilter - sortList first element...: ${export!![0]}")
        if(!up)
        {
            return ArrayList(export!!.asReversed())
        }
        else
        {
            return ArrayList(export!!)
        }
    }

    private fun filterListByCategories(list:ArrayList<Food>):ArrayList<Food>
    {
      var export:ArrayList<Food> = ArrayList()
        for(i in list)
            for(j in categories)
            {
                if(i.category == j)
                    export.add(i)
            }

        return export
    }

    private fun filterByAllowedFood(list:ArrayList<Food>):ArrayList<Food>
    {
        var export:ArrayList<Food> = ArrayList()
        for(i in list)
        {
            if(i.kcal <= sharePrefs.maxAllowedKcal)
            {
                export.add(i)
            }
        }

        return export

    }

    private fun filterByUserFood(list:ArrayList<Food>):ArrayList<Food>
    {
        var export:ArrayList<Food> = ArrayList()
        for(i in list)
        {
            if(i.id.contains("u"))
            {
                export.add(i)
            }
        }

        return export
    }

    private fun filterByFavourites(list:ArrayList<Food>):ArrayList<Food>
    {
        // Hier noch alles impementieren...
        return list
    }


    // Setter:
    fun setCategories(newCategories:ArrayList<String>)
    {
        categories = newCategories
        callInterFace()
    }

    fun setAllowedFood(newAllowedFood:Boolean)
    {
        allowedFood = newAllowedFood
        callInterFace()
    }

    fun setUserFood(newUserFood:Boolean)
    {
        userFood = newUserFood
        callInterFace()
    }

    fun setFavourites(newFavourites:Boolean)
    {
        favourites = newFavourites
        callInterFace()
    }


    fun setSortItem(item:String,up:Boolean)
    {
        sortingItem = item
        this.up = up
        callInterFace()
    }


    fun callInterFace()
    {
        if(mListener!=null)
        {
            mListener.setOnFilterItemsListener(getFilteredFoodList())
        }
    }

    interface OnFilterItemsListener
    {
        fun setOnFilterItemsListener(foodList:ArrayList<Food>)
    }

    fun setOnFilterItemsListener(mListener:OnFilterItemsListener)
    {
        this.mListener = mListener
    }



}