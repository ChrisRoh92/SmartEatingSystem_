package de.rohnert.smeasy.backend.repository.subrepositories.food

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import com.example.roomdatabaseexample.backend.repository.subrepositories.food.FoodProcessor
import de.rohnert.smeasy.backend.databases.food_database.appfood_database.AppFoodDao2
import de.rohnert.smeasy.backend.databases.food_database.appfood_database.AppFoodDataBaseProvider2

class FoodRepository2(var application: Application)
{
    private var appFoodDao2: AppFoodDao2
    private var foodProcessor:FoodProcessor = FoodProcessor(application)

    init {
        var appDB = AppFoodDataBaseProvider2.getDatabase(application)
        appFoodDao2 = appDB.appFoodDao2()
        foodProcessor

    }


    suspend fun getAppFoodList():ArrayList<Food>
    {
        return ArrayList(appFoodDao2.getAppFoodData())
    }

    suspend fun getLiveFoodList(): LiveData<List<Food>>
    {
        return appFoodDao2.getAllLiveData()
    }

    suspend fun insertCSVFoodList(list:List<Food>)
    {
        appFoodDao2.insertAll(list)
    }

    suspend fun getFoodByID(id:String):Food
    {
        return appFoodDao2.getDataById(id)
    }



}