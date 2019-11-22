package com.example.roomdatabaseexample.backend.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import backend.helper.DataHandler
import backend.helper.DataStringSplitter
import com.example.roomdatabaseexample.backend.databases.daily_database.Daily
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import com.example.roomdatabaseexample.backend.repository.subrepositories.body.BodyRepository
import com.example.roomdatabaseexample.backend.repository.subrepositories.daily.DailyRepository
import com.example.roomdatabaseexample.backend.repository.subrepositories.food.FoodRepository

class MainRepository(var application: Application)
{

    /*// SubRepositories:
    private var dailyRepository: DailyRepository =
        DailyRepository(
            application
        )
    private var foodRepository: FoodRepository =
        FoodRepository(
            application
        )
    private var bodyRepository: BodyRepository =
        BodyRepository(
            application
        )


    // Diese Variablen kommen später aus den Optionen...
    private var maxKcal = 2500f
    private var maxCarb = 150f
    private var maxProtein = 150f
    private var maxFett = 50f



    /////////////////////////////////////////////////////////////////////////
    // Methoden vom FoodRepository
    // AppFoodList
    fun getAppFoodList():List<Food>
    {
        return foodRepository.getAppFoodList()!!
    }


    fun getUserFoodList():List<Food>
    {
        return  foodRepository.getUserFoodList()
    }

    fun getFoodList():ArrayList<Food>
    {
        return foodRepository.getFoodList()
    }

    fun getLiveFoodList():LiveData<ArrayList<Food>>
    {
        return foodRepository.getLiveFoodList()
    }

    fun getSearchedAndFilteredFoodList(item:String,filterItems:ArrayList<String>):ArrayList<Food>
    {
        return foodRepository.getSearchFilterFoodList(item,filterItems)
    }



    fun getAppFoodById(id:String):Food?
    {
        return foodRepository.getAppFoodById(id)
    }

    // UserFoodList
    fun getUserFoodById(id:String):Food?
    {
        return foodRepository.getUserFoodById(id)
    }
    // Modify UserFoodList

    fun addNewUserFood(food:Food)
    {
        foodRepository.insertNewUserFood(food)
    }
    fun updateUserFood(food:Food)
    {
        foodRepository.updateUserFood(food)
    }

    fun getFoodCateogries():ArrayList<String>
    {
        return foodRepository.getFoodCateogries()
    }
    /////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////
    // Methoden vom DailyRepository
    fun getDailyByDate(date:String):LiveData<Daily>
    {
        if(dailyRepository.checkIfDailyExist(date))
        {
            return dailyRepository.getDailyByDate(date)
        }
        else
        {
            // Leeres Objekt anlegen und abspeichern...
            var daily = Daily(date, ArrayList(), ArrayList(),ArrayList(),ArrayList(),maxKcal,maxCarb,maxProtein,maxFett)
            dailyRepository.createNewDaily(daily)
            // Anschließend aus Datenbank lesen...
            return dailyRepository.getDailyByDate(date)

        }
    }

    fun getOfflineDailyByDate(date:String):Daily
    {
        if(dailyRepository.checkIfDailyExist(date))
        {
            return dailyRepository.getOfflineDailyByDate(date)
        }
        else
        {
            // Leeres Objekt anlegen und abspeichern...
            var daily = Daily(date, ArrayList(), ArrayList(),ArrayList(),ArrayList(),maxKcal,maxCarb,maxProtein,maxFett)
            dailyRepository.createNewDaily(daily)
            // Anschließend aus Datenbank lesen...
            return dailyRepository.getOfflineDailyByDate(date)

        }




        }

    fun updateDaily(daily:Daily)
    {
        dailyRepository.updateDaily(daily)
    }
    /////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////
    // Methoden vom BodyRepository

    /////////////////////////////////////////////////////////////////////////














    /////////////////////////////////////////////////////////////////////////
    // Is Called, when app started initially
    fun insertCSVFoodList()
    {
        // Hier eine Methode einrichten, um aus der .txt zu lesen und Liste zurückzugeben:

        foodRepository.insertCSVFoodList(getCSVFoodList())
    }

    fun getCSVFoodList():ArrayList<Food>
    {
        var handler:DataHandler = DataHandler(application.assets,application.filesDir.toString())
        var splitter = DataStringSplitter()
        var importList:ArrayList<String> = handler.getAppFoodList()
        var csvList:ArrayList<Food> = ArrayList()
        for(i in importList)csvList.add(splitter.createFoodFromString(i))

        return csvList
    }
    /////////////////////////////////////////////////////////////////////////





    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Testing Methods..
    fun getNumberOfAppFoods():Int
    {
        return foodRepository.getNumberOfAppFoods()
    }
*/
}