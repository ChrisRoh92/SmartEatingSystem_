package de.rohnert.smeasy.frontend.foodtracker

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import backend.helper.Helper

import com.example.roomdatabaseexample.backend.databases.daily_database.Daily
import com.example.roomdatabaseexample.backend.databases.daily_database.MealEntry
import com.example.roomdatabaseexample.backend.databases.daily_database.helper.CalcedFood
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import com.example.roomdatabaseexample.backend.repository.MainRepository
import com.example.roomdatabaseexample.backend.repository.subrepositories.daily.DailyProcessor
import com.example.roomdatabaseexample.backend.repository.subrepositories.food.FoodProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodViewModel(application: Application) : AndroidViewModel(application)
{
    // Helper
    private var helper: Helper = Helper()
    private var repository: MainRepository = MainRepository(application)
    private var dailyProcessor = DailyProcessor(application)
    private var foodProcessor = FoodProcessor(application)


    // Daten aus dem Repository
    private lateinit var cDaily:LiveData<Daily>


    private var appFoodList:MutableLiveData<List<Food>>? = MutableLiveData()
    private var userFoodList:MutableLiveData<List<Food>>? = MutableLiveData()
    private var foodList:MutableLiveData<ArrayList<Food>> = MutableLiveData()
    private var localFoodList:ArrayList<Food> = ArrayList()


    //private var currentDaily:LiveData<Daily> = repository.getDailyByDate(helper.getStringFromDate(helper.getCurrentDate()))
    private var localDaily:Daily = repository.getOfflineDailyByDate(helper.getStringFromDate(helper.getCurrentDate()))

    private var breakfastList:MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()
    private var lunchList:MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()
    private var dinnerList:MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()
    private var snackList:MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()


    private var cDate:MutableLiveData<String> = MutableLiveData()


    init {

        // Prüfen, ob bereits die AppFoodList eingetragen wurde...


        if(repository.getAppFoodList().isEmpty())
        {
            suspend fun createNewFoodList()
            {
                repository.insertCSVFoodList()
                appFoodList!!.postValue(ArrayList(repository.getAppFoodList()))
                userFoodList!!.postValue(repository.getUserFoodList())
                localFoodList = ArrayList(appFoodList!!.value!!)

            }

            CoroutineScope(IO).launch {
                loadDaily()
            }

            /*Toast.makeText(application,"AppFoodList ist leer und wird gefüllt...",Toast.LENGTH_SHORT).show()
            foodList.value = repository.getCSVFoodList()
            localFoodList = repository.getCSVFoodList()*/

        }
        // FoodListen erstellen:
        cDate.value = helper.getStringFromDate(helper.getCurrentDate())
        /*appFoodList = ArrayList(repository.getAppFoodList())
        userFoodList!!.value = repository.getUserFoodList()*/



        CoroutineScope(IO).launch {
            if(!repository.getAppFoodList().isEmpty())
            {loadFood()}

            loadDaily()
        }








        //currentDaily.value = repository.getOfflineDailyByDate( cDate.value!!)

        //createFoodList()

        /*breakfastList.!!value!! = ArrayList()
        lunchList.!!value!! = ArrayList()
        dinnerList.!!value!! = ArrayList()
        snackList.!!value = ArrayList()*/
        //createEntryLists()






    }

    private suspend fun loadFood()
    {
        withContext(IO)
        {
            appFoodList!!.postValue(ArrayList(repository.getAppFoodList()))
            userFoodList!!.postValue(repository.getUserFoodList())
            localFoodList = ArrayList(appFoodList!!.value!!)
        }

    }


    private suspend fun loadDaily()
    {
        withContext(IO)
        {
            cDate.postValue(helper.getStringFromDate(helper.getCurrentDate()))
            cDaily = repository.getDailyByDate(cDate.value!!)
            localDaily = repository.getOfflineDailyByDate(cDate.value!!)
            breakfastList!!.postValue(localDaily.breakfastEntry!!)
            lunchList!!.postValue(localDaily.lunchEntry!!)
            dinnerList!!.postValue(localDaily.dinnerEntry!!)
            snackList!!.postValue(localDaily.snackEntry!!)
        }



            /*cDate.value = helper.getStringFromDate(helper.getCurrentDate())
            cDaily = repository.getDailyByDate(cDate.value!!)
            localDaily = repository.getOfflineDailyByDate(cDate.value!!)
            breakfastList!!.value = localDaily.breakfastEntry!!
            lunchList!!.value = localDaily.lunchEntry!!
            dinnerList!!.value = localDaily.dinnerEntry!!
            snackList!!.value = localDaily.snackEntry!!*/



    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Datum einstellen:
    fun getDate():LiveData<String>
    {
        return cDate
    }
    fun setNewDate(newDate:String)
    {
        cDate.value = newDate
        setNewCurrentDaily()
    }
    fun setNewCurrentDaily()
    {
        //currentDaily = repository.getDailyByDate(cDate.value!!)
        localDaily = repository.getOfflineDailyByDate(cDate.value!!)
        createEntryLists()

    }
    fun createEntryLists()
    {
        breakfastList!!.value = localDaily.breakfastEntry!!
        lunchList!!.value = localDaily.lunchEntry!!
        dinnerList!!.value = localDaily.dinnerEntry!!
        snackList!!.value = localDaily.snackEntry!!
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // FOOD
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Getter Methods...
    // Schnittstelle zur Activity/Fragment:
    fun getAppFoodList():ArrayList<Food>
    {
        return ArrayList(appFoodList!!.value!!)
    }
    fun getUserFoodList():LiveData<List<Food>>?
    {
        return userFoodList
    }
    // Die komplette FoodList bekommen...
    fun getFoodList():LiveData<ArrayList<Food>>
    {
        return foodList
    }

    fun getLocalFoodList():ArrayList<Food>
    {
        if(localFoodList.isNullOrEmpty())
        {
            localFoodList = repository.getCSVFoodList()
        }
        return localFoodList
    }

    // Foods by ID
    fun getAppFoodById(id:String):Food?
    {
        return repository.getAppFoodById(id)
    }
    fun getUserFoodById(id:String):Food?
    {
        return repository.getUserFoodById(id)
    }
    fun getFoodById(id:String):Food
    {
        if(id.toLowerCase().contains("a"))
        {
            return getAppFoodById(id)!!
        }
        else
        {
            return getUserFoodById(id)!!
        }
    }
    fun createFoodList()
    {
        var values:ArrayList<Food> = ArrayList(appFoodList!!.value!!)
        values.addAll(ArrayList(userFoodList!!.value!!))
        foodList.value = values
        localFoodList = values

    }
    // Categorien erhalten:
    fun getFoodCategories():ArrayList<String>
    {
        return repository.getFoodCateogries()
    }

    // Modify FoodList...
    fun addNewUserFood()
    {
        var food = Food("u1","Getränke","Fanta","100ml",60f,12.5f,0f,0f,"")
        repository.addNewUserFood(food)
    }

    fun updateUserFood(food:Food)
    {
        repository.updateUserFood(food)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////





    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Daily Methoden:
    fun getDaily():LiveData<Daily>
    {
        return cDaily!!
    }

    fun getOfflineDaily():Daily
    {
        return localDaily
    }

    fun updateDaily(daily:Daily)
    {
        //currentDaily.value = daily
        repository.updateDaily(daily)
    }

    fun addNewMealEntry(foodId:String,menge:Float,meal:String)
    {

        /*fun getMealList(meal:String):ArrayList<MealEntry>
        {
            var export:ArrayList<MealEntry> = ArrayList()
            when(meal)
            {
                "breakfast" -> breakfastList!!.value = ArrayList()
                "lunch" -> export = lunchList!!.value!!
                "dinner" -> export = dinnerList!!.value!!
                else -> export = snackList!!.value!!
            }

            return export
        }
        var localMealEntryList = getMealList(meal)*//*
        var entry = MealEntry(dailyProcessor.getNewMealEntryId(localMealEntryList),foodId,menge)
        localMealEntryList.add(entry)*/
        when(meal)
        {
            "breakfast"->
            {

                var entry = MealEntry(dailyProcessor.getNewMealEntryId(localDaily.breakfastEntry!!),foodId,menge)
                localDaily.breakfastEntry!!.add(entry)
                breakfastList!!.value = localDaily.breakfastEntry
            }
            "lunch"->
            {
                var entry = MealEntry(dailyProcessor.getNewMealEntryId(localDaily.lunchEntry!!),foodId,menge)
                localDaily.lunchEntry!!.add(entry)
                lunchList!!.value = localDaily.lunchEntry
                //lunchList!!.value = localMealEntryList
                //currentDaily!!.value!!.lunchEntry = lunchList!!.value!!
            }
            "dinner"->
            {
                var entry = MealEntry(dailyProcessor.getNewMealEntryId(localDaily.dinnerEntry!!),foodId,menge)
                localDaily.dinnerEntry!!.add(entry)
                dinnerList!!.value = localDaily.dinnerEntry
                //dinnerList!!.value = localMealEntryList
                //currentDaily!!.value!!.dinnerEntry = dinnerList!!.value!!
            }
            "snack"->
            {
                var entry = MealEntry(dailyProcessor.getNewMealEntryId(localDaily.snackEntry!!),foodId,menge)
                localDaily.snackEntry!!.add(entry)
                snackList!!.value = localDaily.snackEntry
                /*snackList!!.value = localMealEntryList*/
                //currentDaily!!.value!!.snackEntry = snackList!!.value!!
            }
        }

        updateDaily(localDaily)





    }

    fun updateMealEntry(entry:MealEntry,meal:String)
    {
        // Steht noch aus
    }

    fun deleteMealEntry(entry:MealEntry,meal:String)
    {
        /*var localEntryList: ArrayList<MealEntry>
        when(meal)
        {
            "breakfast" ->
            {
                localEntryList = breakfastList!!.value!!
                localEntryList.remove(entry)
                breakfastList!!.value = localEntryList
            }
            "lunch" ->
            {
                localEntryList = lunchList!!.value!!
                localEntryList.remove(entry)
                lunchList!!.value = localEntryList
            }
            "dinner" ->
            {
                localEntryList = dinnerList!!.value!!
                localEntryList.remove(entry)
                dinnerList!!.value = localEntryList
            }
            "snack" ->
            {
                localEntryList = snackList!!.value!!
                localEntryList.remove(entry)
                snackList!!.value = localEntryList
            }
        }*/

        /*var localMealEntryList = getMealList(meal)
        var entry = MealEntry(dailyProcessor.getNewMealEntryId(localMealEntryList),foodId,menge)
        localMealEntryList.add(entry)*/


        when(meal)
        {
            "breakfast"->
            {


                localDaily.breakfastEntry!!.remove(entry)
                breakfastList!!.value = localDaily.breakfastEntry

            }
            "lunch"->
            {

                localDaily.lunchEntry!!.remove(entry)
                lunchList!!.value = localDaily.lunchEntry

            }
            "dinner"->
            {

                localDaily.dinnerEntry!!.remove(entry)
                dinnerList!!.value = localDaily.dinnerEntry

            }
            "snack"->
            {

                localDaily.snackEntry!!.remove(entry)
                snackList!!.value = localDaily.snackEntry

            }
        }

        updateDaily(localDaily)

    }

    fun getDailyValues():ArrayList<Float>
    {
        // Steht noch aus
        var export:ArrayList<Float> = arrayListOf(0f,0f,0f,0f)

        var values:ArrayList<ArrayList<Float>> = ArrayList()
        var breakfast:ArrayList<Float> = getMealValues("breakfast")
        var lunch:ArrayList<Float> = getMealValues("lunch")
        var dinner:ArrayList<Float> = getMealValues("dinner")
        var snack:ArrayList<Float> = getMealValues("snack")
        values.add(breakfast)
        values.add(lunch)
        values.add(dinner)
        values.add(snack)
        for(i in values)
        {
            export[0] += i[0]
            export[1] += i[1]
            export[2] += i[2]
            export[3] += i[3]
        }

        return export
    }

    fun getMealValues(meal:String):ArrayList<Float>
    {
        var export:ArrayList<Float> = arrayListOf(0f,0f,0f,0f)
        if(getCalcedFoodsByMeal(meal).isNotEmpty())
        {
            export = dailyProcessor.getMealValues(getCalcedFoodsByMeal(meal))
        }
        //Log.d("Smeasy","FoodViewModel - getMealValues - export: $export")
        return export
    }


    fun getCalcedFoodsByMeal(meal:String):ArrayList<CalcedFood>
    {
        var calcedFoodList:ArrayList<CalcedFood> = ArrayList()
        fun createCalcedFoodList(list:ArrayList<MealEntry>)
        {

            for(i in list)
            {
                calcedFoodList.add(dailyProcessor.getCalcedFood(i.mealID,repository.getAppFoodById(i.id)!!,i.menge))
            }



        }

        when(meal)
        {
            "breakfast" -> createCalcedFoodList(breakfastList!!.value!!)
            "lunch" -> createCalcedFoodList(lunchList!!.value!!)
            "dinner" -> createCalcedFoodList(dinnerList!!.value!!)
            "snack" -> createCalcedFoodList(snackList!!.value!!)
        }


        // Steht noch aus..
        return calcedFoodList
    }

    fun getDailyMaxValues():ArrayList<Float>
    {
        var export:ArrayList<Float> = ArrayList()
        export.add(localDaily.maxKcal)
        export.add(localDaily.maxCarb)
        export.add(localDaily.maxProtein)
        export.add(localDaily.maxFett)
        return export
    }

    fun getBreakfastEntries():LiveData<ArrayList<MealEntry>>
    {
        return breakfastList!!
    }

    fun getLunchEntries():LiveData<ArrayList<MealEntry>>
    {
        return lunchList!!
    }

    fun getDinnerEntries():LiveData<ArrayList<MealEntry>>
    {
        return dinnerList!!
    }

    fun getSnackEntries():LiveData<ArrayList<MealEntry>>
    {
        return snackList!!
    }







    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Testing Methods..
    fun getNumberOfAppFoods():Int
    {
        return repository.getNumberOfAppFoods()
    }





    /*fun getFilteredFoodList():ArrayList<Food>
    {
        var export:ArrayList<Food> = getOriginFoodList()

        if(userFoods)
        {
            export = ArrayList(getUserFoodList()!!.value!!)
        }

        if(allowedFoods)
        {
            export = foodProcessor.getAllowFoodList(export)
        }

        Log.d("Smeasy","getFilterFoodList: filterItems: ${filterItems}")
        export = foodProcessor.getFiltertedFoodList(filterItems,export)
        return export
    }*/


    /*fun getFoodListFiltered(filterItems:ArrayList<String>):ArrayList<Food>
    {
        var export:ArrayList<Food> = ArrayList()
        for(i in localFoodList)
        {
            for(j in filterItems)
            {
                if(j ==  i.category)
                {
                    export.add(i)
                }
            }

        }
        return export
    }



    fun searchInAppFoodList(input:String, filterItem:ArrayList<String>):ArrayList<Food>
    {

        foodList.value = (repository.getSearchedAndFilteredFoodList(input,filterItem))
        localFoodList =  foodList.value!!

        return foodList.value!!

    }*/



}