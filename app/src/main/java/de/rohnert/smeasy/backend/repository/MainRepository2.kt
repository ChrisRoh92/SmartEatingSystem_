package de.rohnert.smeasy.backend.repository

import android.app.Application
import androidx.lifecycle.LiveData
import backend.helper.DataHandler
import backend.helper.DataStringSplitter
import com.example.roomdatabaseexample.backend.databases.daily_database.Daily
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import de.rohnert.smeasy.backend.repository.subrepositories.daily.DailyRepository2
import de.rohnert.smeasy.backend.repository.subrepositories.food.FoodRepository2
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainRepository2(var application: Application)
{
    private var dailyRepository2:DailyRepository2 = DailyRepository2(application)
    private var foodRepository2:FoodRepository2 = FoodRepository2(application)
    private var sharePrefs = SharedAppPreferences(application)



    // Methods from Food Repository2
    suspend fun getAppFoodList():ArrayList<Food>
    {
        return foodRepository2.getAppFoodList()
    }

    suspend fun getLiveAppFoodList():LiveData<List<Food>>
    {
        return foodRepository2.getLiveFoodList()
    }

    suspend fun getFoodById(id:String):Food
    {
        return foodRepository2.getFoodByID(id)
    }

    suspend fun insertCSVFoodList()
    {
        var handler = DataHandler(application.assets,application.filesDir.toString())
        var splitter = DataStringSplitter()
        var importList:ArrayList<String> = handler.getAppFoodList()
        var csvList:ArrayList<Food> = ArrayList()
        for(i in importList)csvList.add(splitter.createFoodFromString(i))
        foodRepository2.insertCSVFoodList(csvList)
    }






























    // Methods from DailyRepository2
    suspend fun getDailyByDate(date:String):Daily
    {
        return dailyRepository2.getDailyByDate(date)
    }

    suspend fun getLiveDailyByDate(date:String):LiveData<Daily>
    {

        return dailyRepository2.getLiveDailyByDate(date)
    }

    suspend fun addNewDaily(daily:Daily)
    {
        dailyRepository2.addNewDaily(daily)
    }

    suspend fun updateDaily(daily:Daily)
    {
        dailyRepository2.updateDaily(daily)
    }

    suspend fun deleteDaily(daily:Daily)
    {
        dailyRepository2.deleteDaily(daily)
    }
}