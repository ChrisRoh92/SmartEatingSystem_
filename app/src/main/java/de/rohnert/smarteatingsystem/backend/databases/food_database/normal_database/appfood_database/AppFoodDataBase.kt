package de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.appfood_database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.Food

@Database(entities = [Food::class], version = 1)
abstract class AppFoodDataBase: RoomDatabase() {
    abstract fun appFoodDao(): AppFoodDao
}


