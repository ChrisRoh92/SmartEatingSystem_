package de.rohnert.smeasy.backend.repository.subrepositories.food

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import com.example.roomdatabaseexample.backend.databases.food_database.userfood_database.UserFoodDao
import com.example.roomdatabaseexample.backend.databases.food_database.userfood_database.UserFoodDataBaseProvider
import com.example.roomdatabaseexample.backend.repository.subrepositories.food.FoodProcessor
import de.rohnert.smeasy.backend.databases.food_database.appfood_database.AppFoodDao2
import de.rohnert.smeasy.backend.databases.food_database.appfood_database.AppFoodDataBaseProvider2
import de.rohnert.smeasy.backend.databases.food_database.favourite_foods.FavFood
import de.rohnert.smeasy.backend.databases.food_database.favourite_foods.FavFoodDao
import de.rohnert.smeasy.backend.databases.food_database.favourite_foods.FavFoodDataBaseProvider

class FoodRepository2(var application: Application)
{
    private var appFoodDao2: AppFoodDao2


    // userfood stuff:
    private var userFoodDao:UserFoodDao

    // favFood Stuff:
    private var favFoodDao:FavFoodDao

    init {
        var appDB = AppFoodDataBaseProvider2.getDatabase(application)
        appFoodDao2 = appDB.appFoodDao2()

        var userDB = UserFoodDataBaseProvider.getDatabase(application)
        userFoodDao = userDB.userFoodDao()

        var favFoodDB = FavFoodDataBaseProvider.getDatabase(application)
        favFoodDao = favFoodDB.favFoodDao()

    }


    // AppFood Stuff
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

    suspend fun getAppFoodByID(id:String):Food
    {
        return appFoodDao2.getDataById(id)
    }



    // UserFood Stuff
    suspend fun getUserFoodList():ArrayList<Food>
    {
        return ArrayList(userFoodDao.getUserFoodData())
    }

    suspend fun addNewUserFood(newFood:Food)
    {
        userFoodDao.insert(newFood)
    }

    suspend fun updateUserFood(updatedFood:Food)
    {
        userFoodDao.update(updatedFood)
    }

    suspend fun deleteUserFood(food:Food)
    {
        userFoodDao.removeFood(food)
    }

    suspend fun getUserFoodByID(id:String):Food
    {
        return userFoodDao.getDataById(id)
    }

    // FavFood Stuff
    suspend fun getFavFoodList():ArrayList<FavFood>
    {
        return ArrayList(favFoodDao.getFavFoods())
    }

    suspend fun addNewFavFood(newFavFood:FavFood)
    {
        favFoodDao.insert(newFavFood)
    }

    suspend fun updateFavFood(updatedFavFood:FavFood)
    {
        favFoodDao.update(updatedFavFood)
    }

    suspend fun deleteFavFood(removedFavFood:FavFood)
    {
        favFoodDao.removeFavFood(removedFavFood)
    }

    suspend fun getFavFoodByID(id:String):Food
    {
        return userFoodDao.getDataById(id)
    }



}