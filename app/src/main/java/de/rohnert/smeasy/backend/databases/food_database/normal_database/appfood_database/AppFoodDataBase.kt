package com.example.roomdatabaseexample.backend.databases.food_database.appfood_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomdatabaseexample.backend.databases.food_database.Food

@Database(entities = [Food::class], version = 1)
abstract class AppFoodDataBase: RoomDatabase() {
    abstract fun appFoodDao(): AppFoodDao
}


object AppFoodDataBaseProvider {

    private var INSTANCE: AppFoodDataBase? = null


    fun getDatabase(context: Context): AppFoodDataBase {
        //val tempInstance = INSTANCE
        if (INSTANCE == null) {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppFoodDataBase::class.java,

                    "appfood_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance

            }
        }
        return INSTANCE!!
    }
}
