package de.rohnert.smarteatingsystem.frontend.foodtracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.rohnert.smarteatingsystem.backend.helper.Helper
import de.rohnert.smarteatingsystem.backend.databases.daily_database.Daily
import de.rohnert.smarteatingsystem.backend.databases.daily_database.MealEntry
import de.rohnert.smarteatingsystem.backend.databases.daily_database.helper.CalcedFood
import de.rohnert.smarteatingsystem.backend.repository.subrepositories.daily.DailyProcessor
import de.rohnert.smarteatingsystem.backend.repository.subrepositories.food.FoodProcessor
import de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.Food
import de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.favourite_foods.FavFood
import de.rohnert.smarteatingsystem.backend.repository.MainRepository2
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedAppPreferences
import de.rohnert.smarteatingsystem.backend.sharedpreferences.SharedPreferencesSmeasyValues
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class FoodViewModel(application: Application) : AndroidViewModel(application)
{

    //Allgemeines:
    private var helper = Helper()
    private var repository = MainRepository2(application)

    private var scope = viewModelScope

    // Prozessoren...
    private var dailyProcess = DailyProcessor()
    private lateinit var foodProcessor: FoodProcessor

    // Zentraler Zugriff auf Einstellungen...
    private var sharePrefs = SharedAppPreferences(application)
    private var smeasyValues = SharedPreferencesSmeasyValues(application)


    /////////////////////////////////////////////////////////////////
    // Datum etc.
    var date:String = helper.getStringFromDate(helper.getCurrentDate())
    var mDate = helper.getCurrentDate()
    // LiveData - Date:
    private var liveDate:MutableLiveData<Date> = MutableLiveData()

    /////////////////////////////////////////////////////////////////
    // FoodChooser Variablen:
    var sMeal = ""

    ////////////////////////////////////////////////////////////////
    // Daten für FoodChooser (FoodListFragment + MealListFragment)
    var onlyFavouriteFood = false // Wird gesetzt, wenn nur Favouriten gezeigt werden sollen!
    var onlyAllowedFoodFilter = false
    var onlyUserFoodFilter = false
    var filterCategory:ArrayList<String> = ArrayList()





    // Offline FoodLists:
    private lateinit var localAppFoodList:ArrayList<ExtendedFood>
    private lateinit var localUserFoodList:ArrayList<ExtendedFood>
    private lateinit var localFoodList:ArrayList<ExtendedFood>
    private lateinit var filteredFoodList:ArrayList<ExtendedFood>
    private lateinit var localFoodCategories:ArrayList<String>
    private var foodListLoaded:MutableLiveData<Int> = MutableLiveData()

    // Daily:
    private lateinit var localDailyList:ArrayList<Daily>
    lateinit var localDaily: Daily

    // Get All CalcedFoodsList()
    private lateinit var allCalcedFoods:ArrayList<ArrayList<ArrayList<CalcedFood>>>

    // FavFoodList:
    private lateinit var localFavFoodList:ArrayList<FavFood>

    // LiveDatas...
    // LiveDate, wenn ein neues UserFood eingetragen/verändert/gelöscht wird...
    private var updatedFoodList:MutableLiveData<Int> = MutableLiveData()
    // Daily-Stuff
    private var allMeallistsReady:MutableLiveData<Int> = MutableLiveData()
    private var breakfastList: MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()
    private var lunchList: MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()
    private var dinnerList: MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()
    private var snackList: MutableLiveData<ArrayList<MealEntry>>? = MutableLiveData()
    private var allCalcedFood: MutableLiveData<Int> = MutableLiveData()




    init {
        Log.d(TAG,"FoodViewModel - init {}")
        liveDate.value = helper.getCurrentDate()
        CoroutineScope(IO).launch {

            setFoodList()
            // Am Anfang ist diese Liste leer
            // Sollte das der Fall sein, wird aus einer CSV Datei, die Datenbank
            // initial gefüllt:
            if(localAppFoodList.isNullOrEmpty())
            {
                setCSVFoodList()
                setFoodList()

            }

            setLocalDaily()
            foodProcessor = FoodProcessor(application, localFoodList)

            // Hier so komisch, damit direkt mit livedata.value = ... gearbeitet werden kann
            withContext(Main)
            {
                //setFavFoodList()
                createEntryLists()
            }

        }





    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Init Private Methoden


    private suspend fun setLocalDaily()
    {

        suspend fun getDailyWithDate(date:String):Daily
        {
            var exist = false
            var export: Daily? = null
            for(i in localDailyList)
            {
                if(i.date == date)
                {
                    exist = true
                    export = i
                    break
                }
            }
            if(!exist)
            {
                export = repository.getDailyByDate(date)
                localDailyList.add(export)
            }

            return export!!
        }

        // Alle vorhandenen
        localDailyList = repository.getDailyList()
        localDaily = getDailyWithDate(date)

        //localDaily = getDailyFromList(date)
        if(helper.isDateInFuture(helper.getDateFromString(date)))
        {
            if(localDaily.maxKcal != sharePrefs.maxKcal)
            {
                localDaily.maxKcal = sharePrefs.maxKcal
            }
            if(localDaily.maxCarb != sharePrefs.maxCarbValue)
            {
                localDaily.maxCarb = sharePrefs.maxCarbValue
            }
            if(localDaily.maxProtein != sharePrefs.maxProteinValue)
            {
                localDaily.maxProtein = sharePrefs.maxProteinValue
            }
            if(localDaily.maxFett != sharePrefs.maxFettValue)
            {
                localDaily.maxFett = sharePrefs.maxFettValue
            }
        }

        saveToPrefs()

    }

    private suspend fun setFoodList()
    {
        localAppFoodList = repository.getAppFoodList()
        localUserFoodList = repository.getUserFoodList()
        localFoodList = localAppFoodList
        localFoodList.addAll(localUserFoodList)
        filteredFoodList = localFoodList
        localFoodCategories = repository.getFoodCategories()
        filterCategory = localFoodCategories
        setFavFoodList()
        foodListLoaded.postValue(if(foodListLoaded.value == null) 1 else foodListLoaded.value!! + 1)
        // UserFood einbinden...
    }

    private suspend fun setFavFoodList()
    {
        localFavFoodList = repository.getFavFoodList()
    }

    private suspend fun setCSVFoodList()
    {
        repository.insertCSVFoodList()
    }

    private fun createEntryLists()
    {


        lunchList!!.value = localDaily.lunchEntry
        dinnerList!!.value = localDaily.dinnerEntry
        snackList!!.value = localDaily.snackEntry
        breakfastList!!.value = localDaily.breakfastEntry
        allMeallistsReady.value = if(allMeallistsReady.value == null) 1 else allMeallistsReady.value!!+1


    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Date Stuff
    fun setNewDate(newDate:String)
    {
        liveDate.value = helper.getDateFromString(newDate)
        mDate = helper.getDateFromString(newDate)
        date = newDate
        CoroutineScope(Main).launch {

            setLocalDaily()
            createEntryLists()
        }
    }

    fun getLiveDate():LiveData<Date> {return liveDate}
    ///////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    // FoodList Operations..
    suspend fun getAppFoodById(id:String):ExtendedFood?
    {
        var food:ExtendedFood? = null
        withContext(IO)
        {
            if(id.contains("a"))
            {
                food = repository.getAppFoodById(id)
            }
            else
            {
                food = repository.getUserFoodById(id)
            }

        }
        return food

    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Daily Operations...
    fun addNewMealEntry(foodId:String,menge:Float,meal:String)
    {
        // Interne Methode für neue ID des MealEntries...
        fun getNewMealEntryId(iMeal:String):Int
        {
            return when(iMeal) {
                "breakfast" -> getNewMealEntryId(localDaily.breakfastEntry!!)
                "lunch" -> getNewMealEntryId(localDaily.lunchEntry!!)
                "dinner" -> getNewMealEntryId(localDaily.dinnerEntry!!)
                else -> getNewMealEntryId(localDaily.snackEntry!!)


            }
        }

        // Neues MealEntry Objekt erstellen:
        val newEntry = MealEntry(getNewMealEntryId(meal),foodId,menge)

        // newEntry in die jeweilige Liste der heutigen localDaily eintragen....
        when(meal)
        {
            "breakfast" ->
            {

                localDaily.breakfastEntry!!.add(newEntry)
                breakfastList!!.value = localDaily.breakfastEntry
                Log.d(TAG,"addNewMealEntry - breakfast :breakfastList!!.value = ${breakfastList!!.value}")
                Log.d(TAG,"addNewMealEntry - breakfast localDaily = ${localDaily}")

            }
            "lunch" ->
            {

                /*localDaily.lunchEntry = lunchList!!.value!!
                lunchList!!.value!!.add(newEntry)*/
                localDaily.lunchEntry!!.add(newEntry)
                lunchList!!.value = localDaily.lunchEntry
            }
            "dinner" -> {
                /*dinnerList!!.value!!.add(newEntry)
                localDaily.dinnerEntry = dinnerList!!.value!!*/
                localDaily.dinnerEntry!!.add(newEntry)
                dinnerList!!.value = localDaily.dinnerEntry
            }
            "snack" ->
            {
                /*snackList!!.value!!.add(newEntry)
                localDaily.snackEntry = snackList!!.value!!*/
                localDaily.snackEntry!!.add(newEntry)
                snackList!!.value = localDaily.snackEntry
            }
        }

        // Neue Entrylisten erstellen.
        CoroutineScope(Main).launch {
            createEntryLists()
            updateDaily(daily = localDaily)
        }








    }

    fun addNewMealEntries(cfs:ArrayList<CalcedFood>,meal:String)
    {
        fun getNewMealEntryID(iMeal:String):Int
        {
            return when(iMeal) {
                "breakfast" -> dailyProcess.getNewMealEntryId(localDaily.breakfastEntry!!)
                "lunch" -> dailyProcess.getNewMealEntryId(localDaily.lunchEntry!!)
                "dinner" -> dailyProcess.getNewMealEntryId(localDaily.dinnerEntry!!)
                else -> dailyProcess.getNewMealEntryId(localDaily.snackEntry!!)

                // "breakfast" -> dailyProcess.getNewMealEntryId(breakfastList!!.value!!)
                //                "lunch" -> dailyProcess.getNewMealEntryId(lunchList!!.value!!)
                //                "dinner" -> dailyProcess.getNewMealEntryId(dinnerList!!.value!!)
                //                else -> dailyProcess.getNewMealEntryId(snackList!!.value!!)
            }
        }
        var entries:ArrayList<MealEntry> = ArrayList()
        for(i in cfs)
        {
            entries.add(MealEntry(getNewMealEntryID(meal),i.f.id,i.menge))
        }

        when(meal)
        {
            "breakfast" ->
            {

                localDaily.breakfastEntry!!.addAll(entries)
                breakfastList!!.value = localDaily.breakfastEntry

            }
            "lunch" ->
            {

                /*localDaily.lunchEntry = lunchList!!.value!!
                lunchList!!.value!!.add(newEntry)*/
                localDaily.lunchEntry!!.addAll(entries)
                lunchList!!.value = localDaily.lunchEntry
            }
            "dinner" -> {
                /*dinnerList!!.value!!.add(newEntry)
                localDaily.dinnerEntry = dinnerList!!.value!!*/
                localDaily.dinnerEntry!!.addAll(entries)
                dinnerList!!.value = localDaily.dinnerEntry
            }
            "snack" ->
            {
                /*snackList!!.value!!.add(newEntry)
                localDaily.snackEntry = snackList!!.value!!*/
                localDaily.snackEntry!!.addAll(entries)
                snackList!!.value = localDaily.snackEntry
            }
        }

        // Neue Entrylisten erstellen.
        CoroutineScope(Main).launch {
            //createEntryLists()
            updateDaily(daily = localDaily)
        }
    }

    fun removeMealEntry(entry: MealEntry, meal:String)
    {
          when(meal)
            {
                "breakfast" ->
                {
                    localDaily.breakfastEntry!!.remove(entry)
                    breakfastList!!.value = localDaily.breakfastEntry

                }
                "lunch" ->
                {
                    localDaily.lunchEntry!!.remove(entry)
                    lunchList!!.value = localDaily.lunchEntry
                    /*lunchList!!.value!!.remove(entry)
                    daily.value!!.lunchEntry = lunchList!!.value!!*/
                }
                "dinner" -> {
                    localDaily.dinnerEntry!!.remove(entry)
                    dinnerList!!.value = localDaily.dinnerEntry
                }
                "snack" ->
                {
                    localDaily.snackEntry!!.remove(entry)
                    snackList!!.value = localDaily.snackEntry
                }
            }
        // Daily Updaten...
        CoroutineScope(Main).launch {
            //createEntryLists()
            updateDaily(daily = localDaily)
        }

    }

    // Aufrufen, wenn ein MealEntry, neu gemacht werden muss...
    fun updateMealEntry(mealEntry: MealEntry, meal:String)
    {
        fun getMealList():ArrayList<MealEntry>
        {
            when(meal)
            {
                "breakfast" -> return localDaily.breakfastEntry!!
                "lunch" -> return localDaily.lunchEntry!!
                "dinner" -> return localDaily.dinnerEntry!!
                else -> return localDaily.snackEntry!!

            }
        }

        var values = getMealList()
        var pos = -1
        for((index,i) in getMealList().withIndex())
        {
            if(i.id == mealEntry.id)
            {
                pos = index
                getMealList()[pos].menge = mealEntry.menge

                break
            }
        }
        /*if(pos == -1)
        {
            values[pos].menge = mealEntry.menge
        }*/

        when(meal)
        {
            "breakfast" ->
            {
                localDaily.breakfastEntry = values
                breakfastList!!.value = localDaily.breakfastEntry
            }
            "lunch" ->
            {
                localDaily.lunchEntry = values
                lunchList!!.value = localDaily.lunchEntry
            }
            "dinner" ->
            {
                localDaily.dinnerEntry = values
                dinnerList!!.value = localDaily.dinnerEntry
            }
            "snack" ->
            {
                localDaily.snackEntry = values
                snackList!!.value = localDaily.snackEntry
            }
        }

        // Daily Updaten...
        CoroutineScope(Main).launch {
            //createEntryLists()
            updateDaily(daily = localDaily)
        }



            }

    fun changeMealEntry(entry: MealEntry, oldMeal:String, newMeal:String)
    {
        var localEntry = entry
        Log.d("Smeasy","FoodViewModel2 - changeMealEntry - entry: $entry")
        Log.d("Smeasy","FoodViewModel2 - changeMealEntry - oldMeal: $oldMeal")
        Log.d("Smeasy","FoodViewModel2 - changeMealEntry - newMeal: $newMeal")
        CoroutineScope(Main).launch()
        {
            runBlocking {
                removeMealEntry(localEntry,oldMeal)
                addNewMealEntry(localEntry.id,localEntry.menge,newMeal)
            }
        }

    }

    private fun updateDaily(daily: Daily)
    {
        CoroutineScope(IO).launch {
            repository.updateDaily(daily)
        }
    }

    fun getDailyValues(callback: (values: ArrayList<Float>) -> Unit)
    {
        // Steht noch aus
        Log.d(TAG,"FoodViewModel - getDailyValues - current daily = $localDaily")
        CoroutineScope(IO).launch {
            var export:ArrayList<Float> = arrayListOf(0f,0f,0f,0f)
            var values:ArrayList<ArrayList<Float>> = ArrayList()
            for((index,i) in lunchNames.withIndex())
            {
                getMealValues(i) {
                    values.add(it)
                    Log.d(TAG,"FoodViewModel - getDailyValues - values from different lunches($i) with these values = $it")
                    if(index == lunchNames.lastIndex)
                    {
                        for(j  in values)
                        {
                            export[0] += j[0]
                            export[1] += j[1]
                            export[2] += j[2]
                            export[3] += j[3]
                            Log.d(TAG,"FoodViewModel - getDailyValues - values = $i")
                        }
                        Log.d(TAG,"FoodViewModel - getDailyValues - export = $export")
                        callback(export)
                    }
                }

            }


        }

    }

    fun getMealValues(meal:String,callback: (values: ArrayList<Float>) -> Unit)
    {
        var export:ArrayList<Float>
        getCalcedFoodsByMeal(meal) {
            export = dailyProcess.getMealValues(it)
            callback(export)

        }


    }

    fun getCalcedFoodOfDay(callback: (values: ArrayList<Float>) -> Unit)
    {
        CoroutineScope(IO).launch {
            var calcedFoodList:ArrayList<Float> = arrayListOf(0f,0f,0f,0f)
            var foodList:ArrayList<ExtendedFood> = ArrayList()
            var allEntries:ArrayList<MealEntry> = ArrayList()
            allEntries.addAll(localDaily.breakfastEntry!!)
            allEntries.addAll(localDaily.lunchEntry!!)
            allEntries.addAll(localDaily.dinnerEntry!!)
            allEntries.addAll(localDaily.snackEntry!!)
            for(i in allEntries)
            {
                foodList.add(getFoodById(i.id))
            }
            // Calc for all Foods:
            for((index,i) in foodList.withIndex())
            {
                val localValues = getCalcedFoodValues(i,allEntries[index].menge)
                calcedFoodList[0] += localValues[0]
                calcedFoodList[1] += localValues[1]
                calcedFoodList[2] += localValues[2]
                calcedFoodList[3] += localValues[3]
            }
            callback(calcedFoodList)
        }
    }

    fun getCalcedFoodsByMeal(meal:String,callback:(values:ArrayList<CalcedFood>) -> Unit)
    {
        var calcedFoodList:ArrayList<CalcedFood> = ArrayList()

        CoroutineScope(IO).launch {
            suspend fun createCalcedFoodList(list:ArrayList<MealEntry>)
            {
                if(!list.isNullOrEmpty())
                {
                    for(i in list)
                    {
                        calcedFoodList.add(getCalcedFood(i.mealID,getAppFoodById(i.id)!!,i.menge))
                    }

                }




            }

            when(meal)
            {
                "breakfast" -> createCalcedFoodList(localDaily.breakfastEntry!!)
                "lunch" -> createCalcedFoodList(localDaily.lunchEntry!!)
                "dinner" -> createCalcedFoodList(localDaily.dinnerEntry!!)
                "snack" -> createCalcedFoodList(localDaily.snackEntry!!)
            }
            withContext(Main)
            {
                callback(calcedFoodList)
            }

        }



    }

    fun getCalcedFoodsByMeal(entries:ArrayList<MealEntry>):ArrayList<CalcedFood>
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

            createCalcedFoodList(entries)
        }
        return calcedFoodList
    }

    fun getCalcedFoodsofDaily(daily:Daily):ArrayList<ArrayList<CalcedFood>>
    {
        // Abchecken ob Datum überhaupt enthalten ist...
        var export:ArrayList<ArrayList<CalcedFood>> = ArrayList()

        export.add(getCalcedFoodsByMeal(daily.breakfastEntry!!))
        export.add(getCalcedFoodsByMeal(daily.lunchEntry!!))
        export.add(getCalcedFoodsByMeal(daily.dinnerEntry!!))
        export.add(getCalcedFoodsByMeal(daily.snackEntry!!))

        return export






    }

    fun getAllCalcedFoods()
    {
        allCalcedFoods = ArrayList()
        if(localDailyList.isNotEmpty())
        {
            for(i in localDailyList)
            {
                if(!helper.isDateInFuture(helper.getDateFromString(i.date)))
                allCalcedFoods.add(getCalcedFoodsofDaily(i))
            }

        }
        if(allCalcedFood.value != 0)
        {
            allCalcedFood.value = 0
        }
        else
        {

        }
        allCalcedFood.value = allCalcedFood.value?.plus(1)

    }

    fun getDailyMaxValues(callback: (values: ArrayList<Float>) -> Unit)
    {
        sharePrefs = SharedAppPreferences(getApplication())
        CoroutineScope(IO).launch {
            var export:ArrayList<Float> = ArrayList()
            if(localDaily.date == date)
            {
                export.add(localDaily.maxKcal)
                export.add(localDaily.maxCarb)
                export.add(localDaily.maxProtein)
                export.add(localDaily.maxFett)
            }
            else
            {
                runBlocking {
                    export.add(localDaily.maxKcal)
                    export.add(localDaily.maxCarb)
                    export.add(localDaily.maxProtein)
                    export.add(localDaily.maxFett)
                }
            }
            var values:ArrayList<Float> = ArrayList()
            for(i in export)
            {
                if(i == 0f)
                {
                    values.add(25f)
                }
                else
                {
                    values.add(i)
                }
            }
            withContext(Main)
            {
                callback(values)
            }
        }


    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////
    // FoodList Operations.
    fun addNewFood(category:String,name:String,marke:String = "",menge:String ="",
                    unit:String,kcal:Float,carb:Float,protein:Float,fett:Float,ean:String = "",
                    portionName:ArrayList<String>,portionSize:ArrayList<Double>)
    {
        CoroutineScope(IO).launch {
            var newID = foodProcessor.getNextUserFoodList(repository.getUserFoodList())
            var food = ExtendedFood(newID,category,name,marke,menge,unit,ean,kcal,carb,protein,fett,portionName,portionSize)
            repository.addNewUserFood(food)
            withContext(Main)
            {
                runBlocking {
                    setFoodList()
                    setFoodListUpdater()
                }
            }
        }
    }

    fun updateFood( newFood:ExtendedFood)
    {
        // TODO: Implementieren
        scope.launch {
            withContext(IO)
            {
                repository.updateFood(newFood)
            }
        }

    }

    fun addNewPortionToFood(food:ExtendedFood,portionName:String,portionValue:Double)
    {
        // TODO: Implementieren
    }

    // Methode wird aufgerufen, wenn sich wegen der UserFoods etwas ändert...
    private fun setFoodListUpdater()
    {
        if(updatedFoodList.value != 1)
        {
            updatedFoodList.value = 1
        }
        else
        {
            updatedFoodList.value = 1
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // FavFood List:
    fun addNewFavFood(id:String,name:String)
    {
        var favFood = FavFood(id,name)
        if(!localFavFoodList.contains(favFood))
        {
            localFavFoodList.add(favFood)
            CoroutineScope(IO).launch {
                repository.addNewFavFood(favFood)
                Log.d("Smeasy","FoodViewModel - addNewFood")
            }
        }


    }
    fun deleteFavFood(id:String,name:String)
    {
        var favFood = FavFood(id,name)
        var pos = -1
        for((index,i) in localFavFoodList.withIndex())
        {
            if(i.id == id)
            {
                pos = index
                break
            }
        }
        Log.d("Smeasy","FoodViewModel - deleteFavFood pos: $pos")
        if(pos > -1)
        {
            localFavFoodList.removeAt(pos)
            CoroutineScope(IO).launch {
                repository.deleteFavFood(favFood)
            }
        }
    }



    // SharedPreferences Saving:
    private fun saveToPrefs()
    {

        getDailyValues{values ->
            smeasyValues.setNewKcal(values[0])
            smeasyValues.setNewCarbs(values[1])
            smeasyValues.setNewProtein(values[2])
            smeasyValues.setNewFett(values[3])
        }

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

    fun getMeallistReady():LiveData<Int> = allMeallistsReady

    // Getters for FoodLists...
    fun getLocalFoodList():ArrayList<ExtendedFood>
    {
        return localAppFoodList
    }

    fun getFoodList():ArrayList<ExtendedFood>
    {
        return localFoodList
    }

    fun getFoodCategories():ArrayList<String>
    {
        return localFoodCategories
    }

    fun getUpdatedFoodValue():LiveData<Int>
    {
        return updatedFoodList
    }

    fun getFavFoodList():ArrayList<FavFood>
    {
        return localFavFoodList
    }

    ///////////////////////////////////////////
    // Get LiveData Objects:
    fun getFoodListLoaded():LiveData<Int>{return foodListLoaded}







    // Methoden für WeekReportCreator:
    fun getDailyByDate(date:String): Daily?
    {
        var daily: Daily? = null
        runBlocking {
            daily = repository.getDailyByDate(date)
        }

        return daily!!

    }

    fun getFoodById(id:String):ExtendedFood
    {
        var food:ExtendedFood? = null
        runBlocking {
            food = getAppFoodById(id)
        }
        return food!!
    }


    // CalcedFoodStuffs
    fun getAllCalcedFood():LiveData<Int>
    {
        return allCalcedFood
    }

    fun getLocalDailyList():ArrayList<Daily>
    {
        return localDailyList
    }

    fun getAllCalcedFoodList():ArrayList<ArrayList<ArrayList<CalcedFood>>>
    {
        return allCalcedFoods
    }



    /////////////////////////////////////////////////////////////////////////////////////
    // Methoden für FoodListFragment...


    // Get LiveData Stuff:


    // TODO("Das folgende evtl. als SQL Query ausführen!")
    fun searchInFoodList(query:String,simpleSearch:Boolean = true)
    {
        val maxItems = 50
        CoroutineScope(IO).launch {
            var filterItems:ArrayList<ExtendedFood> = ArrayList()
            Log.d(TAG,"FoodViewModel - searchInFoodList() was called!")
            Log.d(TAG,"FoodViewModel - onlyFavouriteFood = $onlyFavouriteFood")
            Log.d(TAG,"FoodViewModel - onlyAllowedFoodFilter = $onlyAllowedFoodFilter")
            Log.d(TAG,"FoodViewModel - onlyUserFoodFilter = $onlyUserFoodFilter")
            fun checkForFilterSettings(food:ExtendedFood):Boolean
            {
                var status = true
                if(onlyFavouriteFood)
                {
                    Log.d(TAG,"FoodViewModel - checkForFilterSettings with onlyFavouriteFood = $onlyFavouriteFood")
                    if(!localFavFoodList.contains(FavFood(food.id,food.name)))
                        return false
                }


                if(onlyAllowedFoodFilter)
                {

                    if(!checkIfFoodIsAllowed(food))
                        return false
                }


                if(onlyUserFoodFilter)
                {

                    if(!localUserFoodList.contains(food))
                        return false
                }


                if(!filterCategory.contains(food.category))
                {
                    return false
                }


                return status
            }

            if(query.isBlank() || query.isEmpty())
            {
                for(i in localFoodList)
                {
                    if(checkForFilterSettings(i))
                        filterItems.add(i)
                }

            }
            else
            {



                for(i in localFoodList)
                {
                    if(i.name.startsWith(query,true) && checkForFilterSettings(i))
                        if(!filterItems.contains(i))
                            filterItems.add(i)

                    //if(simpleSearch && filterItems.size > maxItems) break
                }

                // Filtern

                for(i in localFoodList)
                {
                    //if(simpleSearch && filterItems.size > maxItems) break
                    if((i.name.contains(query,true) || i.marken.contains(query,true) || i.category.contains(query,true))
                        && checkForFilterSettings(i))
                        if(!filterItems.contains(i))
                            filterItems.add(i)


                }
            }




            filteredFoodList = filterItems
            foodListLoaded.postValue(if(foodListLoaded.value == null) 1 else foodListLoaded.value!! + 2)

        }

    }

    fun sortFoodList(name:String,up:Boolean)
    {
        CoroutineScope(IO).launch{
            var export:List<ExtendedFood>? = null
            when(name)
            {
                //"name" -> export = ArrayList(list.sortedWith(compareBy { it.name }))
                "name" ->
                {
                    export = filteredFoodList.sortedWith(compareBy { it.name })
                }
                "category" -> export = filteredFoodList.sortedWith(compareBy { it.category })
                "kcal" -> export = filteredFoodList.sortedWith(compareBy { it.kcal })
                "carb" -> export = filteredFoodList.sortedWith(compareBy { it.carb })
                "protein" -> export = filteredFoodList.sortedWith(compareBy { it.protein })
                "fett" -> export = filteredFoodList.sortedWith(compareBy { it.fett })
            }
            Log.d("Smeasy","FoodListFilter - sortList first element...: ${export!![0]}")
            if(!up)
            {
               filteredFoodList = ArrayList(export.asReversed())
            }
            else
            {
                filteredFoodList =  ArrayList(export)
            }
            foodListLoaded.postValue(if(foodListLoaded.value == null) 1 else foodListLoaded.value!! + 2)

        }

    }


    // Interne Methoden:
    private fun checkIfFoodIsAllowed(food:ExtendedFood):Boolean{
        var status = true

        // Prüfen ob im Kcal Bereich:
        if(sharePrefs.maxAllowedKcal == -1f)
        {
            if(food.kcal >= sharePrefs.minAllowedKcal)
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
            if(food.kcal >= sharePrefs.minAllowedKcal && food.kcal <= sharePrefs.maxAllowedKcal)
            {

            }
            else
            {
                status = false
                return status
            }
        }

        // Prüfen ob im Kcal Bereich:
        if(sharePrefs.maxAllowedCarbs == -1f)
        {
            if(food.carb >= sharePrefs.minAllowedCarbs)
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
            if(food.carb > sharePrefs.minAllowedCarbs && food.carb < sharePrefs.maxAllowedCarbs)
            {

            }
            else
            {
                status = false
                return status
            }
        }

        // Prüfen ob im Kcal Bereich:
        if(sharePrefs.maxAllowedProtein == -1f)
        {
            if(food.protein >= sharePrefs.minAllowedProtein)
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
            if(food.protein > sharePrefs.minAllowedProtein && food.protein < sharePrefs.maxAllowedProtein)
            {

            }
            else
            {
                status = false
                return status
            }
        }
        // Prüfen ob im Kcal Bereich:
        if(sharePrefs.maxAllowedFett == -1f)
        {
            if(food.fett >= sharePrefs.minAllowedFett)
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
            if(food.fett > sharePrefs.minAllowedFett && food.fett < sharePrefs.maxAllowedFett)
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


    fun getFilterFoodList():ArrayList<ExtendedFood>{return filteredFoodList}








































}