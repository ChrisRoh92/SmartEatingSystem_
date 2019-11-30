package de.rohnert.smeasy.backend.repository

import android.app.Application
import androidx.lifecycle.LiveData
import backend.helper.DataHandler
import backend.helper.DataStringSplitter
import com.example.roomdatabaseexample.backend.databases.body_database.Body
import com.example.roomdatabaseexample.backend.databases.daily_database.Daily
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import com.example.roomdatabaseexample.backend.repository.subrepositories.body.BodyRepository
import de.rohnert.smeasy.backend.databases.food_database.favourite_foods.FavFood
import de.rohnert.smeasy.backend.repository.subrepositories.daily.DailyRepository2
import de.rohnert.smeasy.backend.repository.subrepositories.food.FoodRepository2
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainRepository2(var application: Application)
{
    private var dailyRepository2:DailyRepository2 = DailyRepository2(application)
    private var foodRepository:FoodRepository2 = FoodRepository2(application)
    private var bodyRepository = BodyRepository(application)
    private var sharePrefs = SharedAppPreferences(application)



    // Methods from Food Repository2
    // Allgemeine Methoden:
    suspend fun getFoodList():ArrayList<Food>
    {
        var export:ArrayList<Food> = getAppFoodList()
        if(!getUserFoodList().isNullOrEmpty())
        {
            export.addAll(getUserFoodList())
        }
        return export
    }

    ////////////////////////////////////////////////////////////////
    // AppFood Stuff
    suspend fun getAppFoodList():ArrayList<Food>
    {
        return foodRepository.getAppFoodList()
    }
    suspend fun getLiveAppFoodList():LiveData<List<Food>>
    {
        return foodRepository.getLiveFoodList()
    }
    suspend fun getFoodCategories():ArrayList<String>
    {
        var export:ArrayList<String> = ArrayList()
        for(i in getFoodList())
        {
            if(!export.contains(i.category))
            {
                export.add(i.category)
            }
        }


        return export

    }
    suspend fun getAppFoodById(id:String):Food
    {
        return foodRepository.getAppFoodByID(id)
    }
    suspend fun insertCSVFoodList()
    {
        var handler = DataHandler(application.assets,application.filesDir.toString())
        var splitter = DataStringSplitter()
        var importList:ArrayList<String> = handler.getAppFoodList()
        var csvList:ArrayList<Food> = ArrayList()
        for(i in importList)csvList.add(splitter.createFoodFromString(i))
        foodRepository.insertCSVFoodList(csvList)
    }

    ////////////////////////////////////////////////////////////////
    // UserFood Stuff
    suspend fun getUserFoodList():ArrayList<Food>
    {
        return foodRepository.getUserFoodList()
    }
    suspend fun addNewUserFood(newFood:Food)
    {
        foodRepository.addNewUserFood(newFood)
    }
    suspend fun updateUserFood(updatedFood:Food)
    {
        foodRepository.updateUserFood(updatedFood)
    }
    suspend fun deleteUserFood(food:Food)
    {
        foodRepository.deleteUserFood(food)
    }
    suspend fun getUserFoodById(id:String):Food
    {
        return foodRepository.getUserFoodByID(id)
    }

    ////////////////////////////////////////////////////////////////
    // FavFood Stuff
    suspend fun getFavFoodList():ArrayList<FavFood>
    {
        return foodRepository.getFavFoodList()
    }
    suspend fun addNewFavFood(newFavFood: FavFood)
    {
        foodRepository.addNewFavFood(newFavFood)
    }
    suspend fun updateFavFood(updatedFavFood: FavFood)
    {
        foodRepository.updateFavFood(updatedFavFood)
    }
    suspend fun deleteFavFood(removedFavFood: FavFood)
    {
        foodRepository.deleteFavFood(removedFavFood)
    }
    suspend fun getFavFoodByID(id:String):Food
    {
        return foodRepository.getFavFoodByID(id)
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






    // Methoden aus dem BodyRepository
    suspend fun addNewBody(body: Body)
    {
        bodyRepository.addNewBody(body)
    }

    suspend fun updateBody(body: Body)
    {
        bodyRepository.updateBody(body)
    }

    suspend fun deleteBody(body: Body)
    {
        bodyRepository.deleteBody(body)
    }

    suspend fun getBodyList():ArrayList<Body>
    {
        return bodyRepository.getBodyList()
    }

    suspend fun getBodyByDate(date:String): Body
    {
        return bodyRepository.getBodyByDate(date)
    }









}