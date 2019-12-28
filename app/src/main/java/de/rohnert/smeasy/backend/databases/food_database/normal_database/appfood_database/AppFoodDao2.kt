package de.rohnert.smeasy.backend.databases.food_database.normal_database.appfood_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.roomdatabaseexample.backend.databases.food_database.Food

@Dao
interface AppFoodDao2
{
    @Query("SELECT * FROM food")
    fun getAllLiveData(): LiveData<List<Food>>

    @Query("SELECT * FROM food")
    suspend fun getAppFoodData():List<Food>

    @Query("SELECT * FROM food")
    suspend fun getAllData(): List<Food>

    @Query("SELECT * FROM food WHERE id= :id")
    suspend fun getDataById(id:String): Food

    @Query("SELECT COUNT(*) FROM food WHERE id= 'a1'")
    suspend fun getNumberOfEntries():Int

    @Insert
    suspend fun insert(food: Food)

    @Insert
    suspend fun insertAll(list:List<Food>)
}