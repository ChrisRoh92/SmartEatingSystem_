package de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.userfood_extend_database

import androidx.room.*
import de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.ExtendedFood

@Dao
interface UserFoodExtendDao
{
    @Query("SELECT * FROM extend_food")
    suspend fun getUserFoodData():List<ExtendedFood>

    @Query("SELECT * FROM extend_food WHERE id= :id")
    suspend fun getDataById(id:String): ExtendedFood

    @Query("SELECT COUNT(*) FROM extend_food WHERE id= 'u1'")
    suspend fun getNumberOfEntries():Int

    @Insert
    suspend fun insert(food: ExtendedFood)

    @Insert
    suspend fun insertAll(list:List<ExtendedFood>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(food: ExtendedFood)

    @Delete
    suspend fun removeFood(food: ExtendedFood)
}