package de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.appfood_extend_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.ExtendedFood


@Dao
interface AppFoodExtendDao
{
    @Query("SELECT * FROM extend_food")
    fun getAllLiveData(): LiveData<List<ExtendedFood>>

    @Query("SELECT * FROM extend_food")
    suspend fun getAppFoodData():List<ExtendedFood>

    @Query("SELECT * FROM extend_food")
    suspend fun getAllData(): List<ExtendedFood>

    @Query("SELECT * FROM extend_food WHERE id= :id")
    suspend fun getDataById(id:String): ExtendedFood

    @Query("SELECT COUNT(*) FROM extend_food WHERE id= 'a1'")
    suspend fun getNumberOfEntries():Int

    @Query("SELECT * FROM extend_food WHERE name LIKE :argument OR marken = :argument")
    suspend fun getFilteredData(argument:String):List<ExtendedFood>

    @Update
    suspend fun updateFood(newFood:ExtendedFood)

    @Insert
    suspend fun insert(food: ExtendedFood)

    @Insert
    suspend fun insertAll(list:List<ExtendedFood>)
}