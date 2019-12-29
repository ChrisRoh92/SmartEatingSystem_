package de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.userfood_database

import androidx.room.*
import de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.Food

@Dao
interface UserFoodDao
{
    /*@Query("SELECT * FROM food")
    fun getAllLiveData(): LiveData<List<Food>>

    @Query("SELECT * FROM food")
    fun getUserFoodData():List<Food>

    @Query("SELECT * FROM food WHERE id= :id")
    fun getDataById(id:String):Food

    @Insert
    fun insert(food: Food)

    @Update
    fun update(food:Food)*/

    @Query("SELECT * FROM food")
    suspend fun getUserFoodData():List<Food>

    @Query("SELECT * FROM food WHERE id= :id")
    suspend fun getDataById(id:String): Food

    @Query("SELECT COUNT(*) FROM food WHERE id= 'u1'")
    suspend fun getNumberOfEntries():Int

    @Insert
    suspend fun insert(food: Food)

    @Insert
    suspend fun insertAll(list:List<Food>)

    @Update (onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(food: Food)

    @Delete
    suspend fun removeFood(food: Food)




}