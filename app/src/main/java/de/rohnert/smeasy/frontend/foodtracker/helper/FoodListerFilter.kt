package de.rohnert.smeasy.frontend.foodtracker.helper

import android.content.Context
import android.util.Log
import de.rohnert.smeasy.backend.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smeasy.backend.databases.food_database.normal_database.favourite_foods.FavFood
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smeasy.frontend.foodtracker.FoodViewModel2

class FoodListerFilter(var context: Context, var foodViewModel:FoodViewModel2, var extendFilterValues:ArrayList<Float>)
{
    // Default Operators.
    private var foodlist:ArrayList<ExtendedFood> = foodViewModel.getFoodList()
    private var categories:ArrayList<String> = foodViewModel.getFoodCategories()
    private var favFoodList:ArrayList<FavFood> = foodViewModel.getFavFoodList()
    private var allowedFood:Boolean = false
    private var userFood:Boolean = false
    private var favourites:Boolean = false
    private var sortingItem:String = ""
    private var up:Boolean = false
    private var prefs = SharedAppPreferences(context)

    // Extend Filter Values:
    private var minKcal = extendFilterValues[0]
    private var maxKcal = extendFilterValues[1]
    private var minCarb = extendFilterValues[2]
    private var maxCarb = extendFilterValues[3]
    private var minProtein = extendFilterValues[4]
    private var maxProtein = extendFilterValues[5]
    private var minFett = extendFilterValues[6]
    private var maxFett = extendFilterValues[7]

    // Interface
    private lateinit var mListener:OnFilterItemsListener

    fun getFilteredFoodList():ArrayList<ExtendedFood>
    {
        var export:ArrayList<ExtendedFood> = filterListByCategories(foodlist)

        if(allowedFood) export = filterByAllowedFood(export)
        if(userFood) export = filterByUserFood(export)
        if(favourites) export = filterByFavourites(export)
        if(sortingItem!="")
        {
            Log.d("Smeasy","FoodListFilter - sortingItem ist aktiv: $sortingItem")
            var sortList:ArrayList<ExtendedFood> = ArrayList()
            sortList = sortList(export)
            export = sortList
        }
        export = filterByExtendValues(export)

        return export

    }

    fun filterByItemSearch(item:String):ArrayList<ExtendedFood>
    {
        if(item != "")
        {

            var export:ArrayList<ExtendedFood> = ArrayList()
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

    private fun sortList(list:ArrayList<ExtendedFood>):ArrayList<ExtendedFood>
    {
        var export:List<ExtendedFood>? = null
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
            return ArrayList(export.asReversed())
        }
        else
        {
            return ArrayList(export)
        }
    }

    private fun filterListByCategories(list:ArrayList<ExtendedFood>):ArrayList<ExtendedFood>
    {
      var export:ArrayList<ExtendedFood> = ArrayList()
        for(i in list)
            for(j in categories)
            {
                if(i.category == j)
                    export.add(i)
            }

        return export
    }

    private fun filterByAllowedFood(list:ArrayList<ExtendedFood>):ArrayList<ExtendedFood>
    {
        var export:ArrayList<ExtendedFood> = ArrayList()
        for(i in list)
        {
            if(checkIfFoodIsAllowed(i))
            {
                export.add(i)
            }
        }

        return export

    }

    private fun filterByUserFood(list:ArrayList<ExtendedFood>):ArrayList<ExtendedFood>
    {
        var export:ArrayList<ExtendedFood> = ArrayList()
        for(i in list)
        {
            if(i.id.contains("u"))
            {
                export.add(i)
            }
        }

        return export
    }

    private fun filterByFavourites(list:ArrayList<ExtendedFood>):ArrayList<ExtendedFood>
    {
        var export:ArrayList<ExtendedFood> = ArrayList()
        //Log.d("Smeasy","FoodListFilter - filterByFavourites list.size before = ${list.size}")
        for(i in list)
        {
            if(checkIfFoodIsFavourite(i))
            {
                export.add(i)
            }
        }
        //Log.d("Smeasy","FoodListFilter - filterByFavourites export.size before = ${export.size}")
        return export
    }


    private fun filterByExtendValues(list:ArrayList<ExtendedFood>):ArrayList<ExtendedFood>
    {
        var export = list
        var workList:ArrayList<ExtendedFood> = ArrayList()



        Log.d("Smeasy","FoodListFilter - filterByExtendValues minKcal = $minKcal")
        Log.d("Smeasy","FoodListFilter - filterByExtendValues maxKcal = $maxKcal")

        Log.d("Smeasy","FoodListFilter - filterByExtendValues minCarb = $minCarb")
        Log.d("Smeasy","FoodListFilter - filterByExtendValues maxCarb = $maxCarb")

        Log.d("Smeasy","FoodListFilter - filterByExtendValues minProtein = $minProtein")
        Log.d("Smeasy","FoodListFilter - filterByExtendValues maxProtein = $maxProtein")

        Log.d("Smeasy","FoodListFilter - filterByExtendValues minFett = $minFett")
        Log.d("Smeasy","FoodListFilter - filterByExtendValues maxFett = $maxFett")

        // Nach Kalorien Filtern:
        if(minKcal == 0f && maxKcal == 0f)
        {
            workList = export
        }
        else if (maxKcal == 0f && minKcal > 0)
        {
            for(i in export)
            {
                if(i.kcal >= minKcal)
                {
                    workList.add(i)
                }
            }

        }

        else if (minKcal == 0f && maxKcal > 0)
        {
            for(i in export)
            {
                if(i.kcal <= maxKcal)
                {
                    workList.add(i)
                }
            }

        }

        else if(minKcal>0 && maxKcal >0 && maxKcal > minKcal)
            for(i in export)
            {
                if(i.kcal >= minKcal && i.kcal < maxKcal)
                {
                    workList.add(i)
                }
            }

        else
        {
            workList = export
        }
        export = workList
        workList = ArrayList()

        // Nach Kohlenhydrate Filtern:
        if(minCarb == 0f && maxCarb == 0f)
        {
            workList = export
        }
        else if (maxCarb == 0f && minCarb > 0)
        {
            for(i in export)
            {
                if(i.carb >= minCarb)
                {
                    workList.add(i)
                }
            }

        }

        else if (minCarb == 0f && maxCarb > 0)
        {
            for(i in export)
            {
                if(i.carb <= maxCarb)
                {
                    workList.add(i)
                }
            }

        }

        else if(minCarb>0 && maxCarb >0 && maxCarb > minCarb)
            for(i in export)
            {
                if(i.carb >= minCarb && i.carb < maxCarb)
                {
                    workList.add(i)
                }
            }

        else
        {
            workList = export
        }
        export = workList
        workList = ArrayList()

        // Nach Protein Filtern:
        if(minProtein == 0f && maxProtein == 0f)
        {
            workList = export
        }
        else if (maxProtein == 0f && minProtein > 0)
        {
            for(i in export)
            {
                if(i.protein >= minProtein)
                {
                    workList.add(i)
                }
            }

        }

        else if (minProtein == 0f && maxProtein > 0)
        {
            for(i in export)
            {
                if(i.protein <= maxProtein)
                {
                    workList.add(i)
                }
            }

        }

        else if(minProtein>0 && maxProtein >0 && maxProtein > minProtein)
            for(i in export)
            {
                if(i.protein >= minProtein && i.protein < maxProtein)
                {
                    workList.add(i)
                }
            }

        else
        {
            workList = export
        }
        export = workList
        workList = ArrayList()

        // Nach Fett Filtern:
        if(minFett == 0f && maxFett == 0f)
        {
            workList = export
        }
        else if (maxFett == 0f && minFett > 0)
        {
            for(i in export)
            {
                if(i.fett >= minFett)
                {
                    workList.add(i)
                }
            }

        }

        else if (minFett == 0f && maxFett > 0)
        {
            for(i in export)
            {
                if(i.fett <= maxFett)
                {
                    workList.add(i)
                }
            }

        }

        else if(minFett>0 && maxFett >0 && maxFett > minFett)
            for(i in export)
            {
                if(i.fett >= minFett && i.fett < maxFett)
                {
                    workList.add(i)
                }
            }

        else
        {
            workList = export
        }
        export = workList
        workList = ArrayList()








        return export
    }

    private fun checkIfFoodIsAllowed(food:ExtendedFood):Boolean
    {
        var status = true

        // Prüfen ob im Kcal Bereich:
        if(prefs.maxAllowedKcal == -1f)
        {
            if(food.kcal >= prefs.minAllowedKcal)
            {

            }
            else
            {
                status = false
                return status
            }
        }
        else
        {
            if(food.kcal >= prefs.minAllowedKcal && food.kcal <= prefs.maxAllowedKcal)
            {

            }
            else
            {
                status = false
                return status
            }
        }

        // Prüfen ob im Kcal Bereich:
        if(prefs.maxAllowedCarbs == -1f)
        {
            if(food.carb > prefs.minAllowedCarbs)
            {

            }
            else
            {
                status = false
                return status
            }
        }
        else
        {
            if(food.carb > prefs.minAllowedCarbs && food.carb < prefs.maxAllowedCarbs)
            {

            }
            else
            {
                status = false
                return status
            }
        }

        // Prüfen ob im Kcal Bereich:
        if(prefs.maxAllowedProtein == -1f)
        {
            if(food.protein > prefs.minAllowedProtein)
            {

            }
            else
            {
                status = false
                return status
            }
        }
        else
        {
            if(food.protein > prefs.minAllowedProtein && food.protein < prefs.maxAllowedProtein)
            {

            }
            else
            {
                status = false
                return status
            }
        }
        // Prüfen ob im Kcal Bereich:
        if(prefs.maxAllowedFett == -1f)
        {
            if(food.fett > prefs.minAllowedFett)
            {

            }
            else
            {
                status = false
                return status
            }
        }
        else
        {
            if(food.fett > prefs.minAllowedFett && food.fett < prefs.maxAllowedFett)
            {

            }
            else
            {
                status = false
                return status
            }
        }





        return status
    }



    private fun checkIfFoodIsFavourite(food:ExtendedFood):Boolean
    {
        var check = false
        for(i in favFoodList)
        {
            if(i.id == food.id)
            {
                check = true
                break
            }
        }
        return check
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
        Log.d("Smeasy","FoodListFilter - setFavourites newFavourites = $newFavourites && favourites = $favourites")
        if(favourites!=newFavourites)
        {
            favourites = newFavourites
            callInterFace()
        }

    }


    fun setSortItem(item:String,up:Boolean)
    {
        sortingItem = item
        this.up = up
        callInterFace()
    }

    // Neue LebensmittelListe abrufen...
    fun setNewFoodList()
    {
        foodlist = foodViewModel.getFoodList()
        categories = foodViewModel.getFoodCategories()
        callInterFace()
    }
    // Neue Favouriten Setzen
    fun setNewFavFoodList()
    {
        favFoodList = foodViewModel.getFavFoodList()
    }
    // Neue Extended Values setzen:
    fun setNewExtendedFilterValues(newValues:ArrayList<Float>)
    {
        extendFilterValues = newValues
        minKcal = extendFilterValues[0]
        maxKcal = extendFilterValues[1]
        minCarb = extendFilterValues[2]
        maxCarb = extendFilterValues[3]
        minProtein = extendFilterValues[4]
        maxProtein = extendFilterValues[5]
        minFett = extendFilterValues[6]
        maxFett = extendFilterValues[7]
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
        fun setOnFilterItemsListener(foodList:ArrayList<ExtendedFood>)
    }

    fun setOnFilterItemsListener(mListener:OnFilterItemsListener)
    {
        this.mListener = mListener
    }



}