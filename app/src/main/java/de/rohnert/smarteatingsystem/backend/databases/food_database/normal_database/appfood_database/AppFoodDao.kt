package de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.appfood_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.Food


@Dao
interface AppFoodDao
{

    @Query("SELECT * FROM food")
    fun getAllLiveData(): LiveData<List<Food>>

    @Query("SELECT * FROM food")
    fun getAppFoodData():List<Food>

    @Query("SELECT * FROM food")
    fun getAllData(): List<Food>

    @Query("SELECT * FROM food WHERE id= :id")
    fun getDataById(id:String): Food

    @Query("SELECT COUNT(*) FROM food WHERE id= 'a1'")
    fun getNumberOfEntries():Int

    @Insert
    fun insert(food: Food)

    @Insert
    fun insertAll(list:List<Food>)
}