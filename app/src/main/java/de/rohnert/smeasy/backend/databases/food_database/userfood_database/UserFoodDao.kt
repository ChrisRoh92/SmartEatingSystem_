package com.example.roomdatabaseexample.backend.databases.food_database.userfood_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.roomdatabaseexample.backend.databases.food_database.Food

@Dao
interface UserFoodDao
{
    @Query("SELECT * FROM food")
    fun getAllLiveData(): LiveData<List<Food>>

    @Query("SELECT * FROM food")
    fun getUserFoodData():List<Food>

    @Query("SELECT * FROM food WHERE id= :id")
    fun getDataById(id:String):Food

    @Insert
    fun insert(food: Food)

    @Update
    fun update(food:Food)
}