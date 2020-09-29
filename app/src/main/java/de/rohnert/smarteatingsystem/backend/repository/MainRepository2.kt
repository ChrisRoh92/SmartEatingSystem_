package de.rohnert.smarteatingsystem.backend.repository

import android.app.Application
import de.rohnert.smarteatingsystem.backend.helper.DataHandler
import de.rohnert.smarteatingsystem.backend.helper.DataStringSplitter
import de.rohnert.smarteatingsystem.backend.databases.body_database.Body
import de.rohnert.smarteatingsystem.backend.databases.daily_database.Daily
import de.rohnert.smarteatingsystem.backend.repository.subrepositories.body.BodyRepository
import de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.favourite_foods.FavFood
import de.rohnert.smarteatingsystem.backend.repository.subrepositories.daily.DailyRepository2
import de.rohnert.smarteatingsystem.backend.repository.subrepositories.food.ExtendedFoodRepository

class MainRepository2(var application: Application)
{
    private var dailyRepository2:DailyRepository2 = DailyRepository2(application)
    private var foodRepository:ExtendedFoodRepository = ExtendedFoodRepository(application)
    private var bodyRepository = BodyRepository(application)


    // Methods from Food Repository2
    // Allgemeine Methoden:
    suspend fun getFoodList():ArrayList<ExtendedFood>
    {
        var export:ArrayList<ExtendedFood> = getAppFoodList()
        if(!getUserFoodList().isNullOrEmpty())
        {
            export.addAll(getUserFoodList())
        }
        return export
    }

    ////////////////////////////////////////////////////////////////
    // AppFood Stuff
    suspend fun getAppFoodList():ArrayList<ExtendedFood>
    {
        return foodRepository.getAppFoodList()
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
    suspend fun getAppFoodById(id:String):ExtendedFood
    {
        return foodRepository.getAppFoodByID(id)
    }
    suspend fun insertCSVFoodList()
    {
        var handler = DataHandler(application.assets,application.filesDir.toString())
        var splitter = DataStringSplitter()
        var importList:ArrayList<String> = handler.getAppFoodList()
        var csvList:ArrayList<ExtendedFood> = ArrayList()
        for(i in importList)csvList.add(splitter.getExtendedFoodFromString(i))
        foodRepository.insertCSVFoodList(csvList)
    }

    ////////////////////////////////////////////////////////////////
    // UserFood Stuff
    suspend fun getUserFoodList():ArrayList<ExtendedFood>
    {
        return foodRepository.getUserFoodList()
    }
    suspend fun addNewUserFood(newFood:ExtendedFood)
    {
        foodRepository.addNewUserFood(newFood)
    }
    suspend fun updateUserFood(updatedFood:ExtendedFood)
    {
        foodRepository.updateUserFood(updatedFood)
    }

    suspend fun getUserFoodById(id:String):ExtendedFood
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

    suspend fun deleteFavFood(removedFavFood: FavFood)
    {
        foodRepository.deleteFavFood(removedFavFood)
    }

    // Methods from DailyRepository2
    suspend fun getDailyByDate(date:String): Daily
    {

        return dailyRepository2.getDailyByDate(date)
    }

    suspend fun getDailyList():ArrayList<Daily>
    {
        return dailyRepository2.getDailyList()
    }

    suspend fun updateDaily(daily: Daily)
    {
        dailyRepository2.updateDaily(daily)
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