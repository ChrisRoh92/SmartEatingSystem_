package de.rohnert.smeasy.backend.databases.food_database.favourite_foods

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.roomdatabaseexample.backend.databases.food_database.Food


@Dao
interface FavFoodDao
{
    @Query("SELECT * FROM favFood")
    suspend fun getFavFoods():List<FavFood>


    @Query("SELECT * FROM favFood WHERE foodID= :id")
    suspend fun getFavFoodById(id:String): FavFood

    @Query("SELECT COUNT(*) FROM favFood")
    suspend fun getNumberOfEntries():Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(favFood:FavFood)

    @Delete
    suspend fun removeFavFood(favFood:FavFood)

    @Insert
    suspend fun insert(favFood:FavFood)

    @Insert
    suspend fun insertAll(list:List<FavFood>)
}