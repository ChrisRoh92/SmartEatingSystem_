package de.rohnert.smeasy.backend.databases.food_database.normal_database.appfood_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.rohnert.smeasy.backend.databases.food_database.normal_database.Food

@Database(entities = [Food::class], version = 1)
abstract class AppFoodDataBase: RoomDatabase() {
    abstract fun appFoodDao(): AppFoodDao
}


