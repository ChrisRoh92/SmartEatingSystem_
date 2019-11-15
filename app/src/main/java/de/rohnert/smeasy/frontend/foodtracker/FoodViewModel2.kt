package de.rohnert.smeasy.frontend.foodtracker

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import backend.helper.Helper
import com.example.roomdatabaseexample.backend.databases.daily_database.Daily
import com.example.roomdatabaseexample.backend.databases.daily_database.MealEntry
import com.example.roomdatabaseexample.backend.databases.daily_database.helper.CalcedFood
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import com.example.roomdatabaseexample.backend.repository.subrepositories.daily.DailyProcessor
import com.example.roomdatabaseexample.backend.repository.subrepositories.food.FoodProcessor
import de.rohnert.smeasy.backend.repository.MainRepository2
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class FoodViewModel2(application: Application) : AndroidViewModel(application)
{

    //Allgemeines:
    private var helper = Helper()
    private var repository = MainRepository2(application)
    private var dailyProcess = DailyProcessor(application)
    private var foodProcessor = FoodProcessor(application)
    private var sharePrefs = SharedAppPreferences(application)


    var date:String = helper.getStringFromDate(helper.getCurrentDate())
    //private var lDate:MutableLiveData<String> = MutableLiveData()
    // Daily-Stuff
    private lateinit var daily:LiveData<Daily>
    private var breakfastList: MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()
    private var lunchList: MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()
    private var dinnerList: MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()
    private var snackList: MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()

    // FoodLists:
    //private lateinit  var appFoodList:LiveData<List<Food>>
    private lateinit var localAppFoodList:ArrayList<Food>

    // Daily:
    private lateinit var localDaily:Daily




    init {

        CoroutineScope(IO).launch {
            setFoodList()

            if(localAppFoodList.isNullOrEmpty())
            {
                setCSVFoodList()
                setFoodList()
                //Toast.makeText(application,"Food Liste wurde angelegt...",Toast.LENGTH_SHORT).show()
                Log.d("Smeasy","FoodViewModel2 - init - FoodListe aus CSV wurde angelegt....")
            }
            withContext(Main)
            {
                setLocalDaily()
                createEntryLists()
            }

        }


        /*CoroutineScope(IO).launch {
            getDaily(date)
            setFoodList()
            if(appFoodList.value.isNullOrEmpty())
            {
                setCSVFoodList()
                setFoodList()
            }
            createEntryLists()
            lDate.value = date

        }*/


    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Init Private Methoden
    private suspend fun getDaily(date:String)
    {
        daily = repository.getLiveDailyByDate(date)

        /*if(daily.value == null)
        {
            CoroutineScope(IO).launch {
                var newDaily = Daily(date, ArrayList(), ArrayList(),ArrayList(),ArrayList(),sharePrefs.maxKcal,sharePrefs.maxCarb,sharePrefs.maxProtein,sharePrefs.maxFett)
                repository.addNewDaily(newDaily)
                daily = repository.getLiveDailyByDate(date)

            }
        }*/
    }

    private suspend fun setLocalDaily()
    {
        localDaily = repository.getDailyByDate(date)
    }

    private suspend fun setFoodList()
    {
        localAppFoodList = repository.getAppFoodList()
        //appFoodList = repository.getLiveAppFoodList()
    }

    private suspend fun setCSVFoodList()
    {
        repository.insertCSVFoodList()
    }

    private fun createEntryLists()
    {


        //breakfastList!!.postValue(localDaily.breakfastEntry)
        runBlocking {

            lunchList!!.value = localDaily.lunchEntry
            dinnerList!!.value = localDaily.dinnerEntry
            snackList!!.value = localDaily.snackEntry
            breakfastList!!.value = localDaily.breakfastEntry
        }

        /*lunchList!!.postValue(localDaily.lunchEntry)
        dinnerList!!.postValue(localDaily.dinnerEntry)
        snackList!!.postValue(localDaily.snackEntry)*/

        Log.d("Smeasy","FoodViewModel2 createEntryList: localDaily.breakfastEntry: ${localDaily.breakfastEntry}")
        Log.d("Smeasy","FoodViewModel2 createEntryList: localDaily.lunchEntry: ${localDaily.lunchEntry}")
        Log.d("Smeasy","FoodViewModel2 createEntryList: localDaily.dinnerEntry: ${localDaily.dinnerEntry}")
        Log.d("Smeasy","FoodViewModel2 createEntryList: localDaily.snackEntry: ${localDaily.snackEntry}")

        Log.d("Smeasy","FoodViewModel2 createEntryList: breakfastList: ${breakfastList!!.value}")
        Log.d("Smeasy","FoodViewModel2 createEntryList: lunchList: ${lunchList!!.value}")
        Log.d("Smeasy","FoodViewModel2 createEntryList: dinnerList: ${dinnerList!!.value}")
        Log.d("Smeasy","FoodViewModel2 createEntryList: snackList: ${snackList!!.value}")

    }

    private suspend fun initDaily(date:String)
    {
        getDaily(date)
        createEntryLists()
    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Set New Date...
    fun setNewDate(newDate:String)
    {
        date = newDate
        CoroutineScope(IO).launch {
            runBlocking {
                setLocalDaily()
            }
            withContext(Main)
            {
                createEntryLists()
            }

        }

        /*CoroutineScope(IO).launch()
        {
            getDaily(date)
        }*/
    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    // FoodList Operations..
    suspend fun getAppFoodById(id:String):Food?
    {
        var food:Food? = null

        withContext(IO)
        {
            food = repository.getFoodById(id)
        }
        /*CoroutineScope(IO).launch {

        }*/

        return food

    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Daily Operations...
    fun addNewMealEntry(foodId:String,menge:Float,meal:String)
    {
        // Interne Methode für neue ID des MealEntries...
        fun getNewMealEntry(iMeal:String):Int
        {
            return when(iMeal) {
                "breakfast" -> dailyProcess.getNewMealEntryId(breakfastList!!.value!!)
                "lunch" -> dailyProcess.getNewMealEntryId(lunchList!!.value!!)
                "dinner" -> dailyProcess.getNewMealEntryId(dinnerList!!.value!!)
                else -> dailyProcess.getNewMealEntryId(snackList!!.value!!)
            }
        }

        var newEntry = MealEntry(getNewMealEntry(meal),foodId,menge)
        Log.d("Smeasy","FoodViewModel2 - addNewMealEntry - newEntry = $newEntry")

        when(meal)
        {
            "breakfast" ->
            {

                localDaily.breakfastEntry!!.add(newEntry)

            }
            "lunch" ->
            {

                /*localDaily.lunchEntry = lunchList!!.value!!
                lunchList!!.value!!.add(newEntry)*/
                localDaily.lunchEntry!!.add(newEntry)
            }
            "dinner" -> {
                /*dinnerList!!.value!!.add(newEntry)
                localDaily.dinnerEntry = dinnerList!!.value!!*/
                localDaily.dinnerEntry!!.add(newEntry)
            }
            "snack" ->
            {
                /*snackList!!.value!!.add(newEntry)
                localDaily.snackEntry = snackList!!.value!!*/
                localDaily.snackEntry!!.add(newEntry)
            }
        }

        // Neue Entrylisten erstellen.
        CoroutineScope(Main).launch {
            createEntryLists()
            updateDaily(daily = localDaily)
        }


        // Daily Updaten...






    }

    fun removeMealEntry(entry:MealEntry,meal:String)
    {
          when(meal)
            {
                "breakfast" ->
                {
                    localDaily.breakfastEntry!!.remove(entry)

                }
                "lunch" ->
                {
                    localDaily.lunchEntry!!.remove(entry)
                    /*lunchList!!.value!!.remove(entry)
                    daily.value!!.lunchEntry = lunchList!!.value!!*/
                }
                "dinner" -> {
                    localDaily.dinnerEntry!!.remove(entry)
                }
                "snack" ->
                {
                    localDaily.snackEntry!!.remove(entry)
                }
            }
        // Daily Updaten...
        CoroutineScope(Main).launch {
            createEntryLists()
            updateDaily(daily = localDaily)
        }

    }

    private fun updateDaily(daily:Daily)
    {
        CoroutineScope(IO).launch {
            repository.updateDaily(daily)
        }
    }

    fun getDailyValues():ArrayList<Float>
    {
        // Steht noch aus
        var export:ArrayList<Float> = arrayListOf(0f,0f,0f,0f)
        runBlocking {
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
        }


        return export
    }

    fun getMealValues(meal:String):ArrayList<Float>
    {
        var export:ArrayList<Float> = arrayListOf(0f,0f,0f,0f)
        if(getCalcedFoodsByMeal(meal).isNotEmpty())
        {
            export = dailyProcess.getMealValues(getCalcedFoodsByMeal(meal))
        }
        //Log.d("Smeasy","FoodViewModel - getMealValues - export: $export")
        return export
    }


    fun getCalcedFoodsByMeal(meal:String):ArrayList<CalcedFood>
    {
        var calcedFoodList:ArrayList<CalcedFood> = ArrayList()
        runBlocking {
            suspend fun createCalcedFoodList(list:ArrayList<MealEntry>)
            {
                if(!list.isNullOrEmpty())
                {
                    //Log.d("Smeas")

                    for(i in list)
                    {
                        calcedFoodList.add(dailyProcess.getCalcedFood(i.mealID,getAppFoodById(i.id)!!,i.menge))
                    }

                }




            }

            when(meal)
            {
                "breakfast" -> createCalcedFoodList(breakfastList!!.value!!)
                "lunch" -> createCalcedFoodList(lunchList!!.value!!)
                "dinner" -> createCalcedFoodList(dinnerList!!.value!!)
                "snack" -> createCalcedFoodList(snackList!!.value!!)
            }
        }



        /*CoroutineScope(Main).launch {

            fun createCalcedFoodList(list:ArrayList<MealEntry>)
            {
                if(!list.isNullOrEmpty())
                {
                    //Log.d("Smeas")
                    for(i in list)
                    {
                        calcedFoodList.add(dailyProcess.getCalcedFood(i.mealID,getAppFoodById(i.id)!!,i.menge))
                    }

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

        }*/
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
///////////////////////////////////////////////////////////////////////////////////////////



    ///////////////////////////////////////////////////////////////////////////////////////////
    // Getters for LiveData....
    fun getDaily():LiveData<Daily>
    {
        return daily
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

    /*fun getFoodList():LiveData<List<Food>>
    {
        return appFoodList
    }*/

    fun getLocalFoodList():ArrayList<Food>
    {
        return localAppFoodList
    }

    /*fun getDate():LiveData<String>
    {
        return lDate
    }*/













































}