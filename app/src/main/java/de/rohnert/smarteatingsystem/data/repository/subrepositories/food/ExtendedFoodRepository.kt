package de.rohnert.smarteatingsystem.data.repository.subrepositories.food

import android.app.Application
import androidx.lifecycle.LiveData
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.appfood_extend_database.AppFoodExtendDao
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.appfood_extend_database.AppFoodExtendDataBaseProvider
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.userfood_extend_database.UserFoodExtendDao
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.userfood_extend_database.UserFoodExtendDataBaseProvider
import de.rohnert.smarteatingsystem.data.databases.food_database.normal_database.favourite_foods.FavFood
import de.rohnert.smarteatingsystem.data.databases.food_database.normal_database.favourite_foods.FavFoodDao
import de.rohnert.smarteatingsystem.data.databases.food_database.normal_database.favourite_foods.FavFoodDataBaseProvider

class ExtendedFoodRepository(var application: Application)
{
    // AppFood Stuff
    private var appFoodExtendedDao:AppFoodExtendDao

    // UserFood Stuff:
    private var userFoodExtendDao:UserFoodExtendDao

    // favFood Stuff:
    private var favFoodDao: FavFoodDao


    init {
        var appDB = AppFoodExtendDataBaseProvider.getDatabase(application)
        appFoodExtendedDao = appDB.appFoodExtendDao()

        var userFoodDB = UserFoodExtendDataBaseProvider.getDatabase(application)
        userFoodExtendDao = userFoodDB.userFoodExtendDao()

        var favFoodDB = FavFoodDataBaseProvider.getDatabase(application)
        favFoodDao = favFoodDB.favFoodDao()
    }

    ///////////////////////////////////////////////////////////////////////////////
    // AppFoodStuff
    suspend fun getAppFoodList():ArrayList<ExtendedFood>
    {
        return ArrayList(appFoodExtendedDao.getAppFoodData())
    }

    fun getLiveFoodList(): LiveData<List<ExtendedFood>>
    {
        return appFoodExtendedDao.getAllLiveData()
    }

    suspend fun insertCSVFoodList(list:List<ExtendedFood>)
    {
        appFoodExtendedDao.insertAll(list)
    }

    suspend fun getAppFoodByID(id:String): ExtendedFood
    {
        return appFoodExtendedDao.getDataById(id)
    }
    ///////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    // UserFood Stuff
    suspend fun getUserFoodList():ArrayList<ExtendedFood>
    {
        return ArrayList(userFoodExtendDao.getUserFoodData())
    }

    suspend fun addNewUserFood(newFood:ExtendedFood)
    {
        userFoodExtendDao.insert(newFood)
    }

    suspend fun updateUserFood(updatedFood:ExtendedFood)
    {
        userFoodExtendDao.update(updatedFood)
    }

    suspend fun deleteUserFood(food:ExtendedFood)
    {
        userFoodExtendDao.removeFood(food)
    }

    suspend fun getUserFoodByID(id:String):ExtendedFood
    {
        return userFoodExtendDao.getDataById(id)
    }
    ///////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
    // FavFood Stuff
    suspend fun getFavFoodList():ArrayList<FavFood>
    {
        return ArrayList(favFoodDao.getFavFoods())
    }

    suspend fun addNewFavFood(newFavFood: FavFood)
    {
        favFoodDao.insert(newFavFood)
    }

    suspend fun updateFavFood(updatedFavFood: FavFood)
    {
        favFoodDao.update(updatedFavFood)
    }

    suspend fun deleteFavFood(removedFavFood: FavFood)
    {
        favFoodDao.removeFavFood(removedFavFood)
    }

    suspend fun getFavFoodByID(id:String):ExtendedFood
    {
        return userFoodExtendDao.getDataById(id)
    }

    suspend fun updateFood(newFood: ExtendedFood) {

        appFoodExtendedDao.updateFood(newFood)

    }
    ///////////////////////////////////////////////////////////////////////////////
}