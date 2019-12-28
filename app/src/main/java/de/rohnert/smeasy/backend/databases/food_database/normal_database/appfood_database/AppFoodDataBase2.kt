package de.rohnert.smeasy.backend.databases.food_database.normal_database.appfood_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomdatabaseexample.backend.databases.food_database.Food

@Database(entities = [Food::class], version = 1)
abstract class AppFoodDataBase2: RoomDatabase() {

    abstract fun appFoodDao2(): AppFoodDao2
}


object AppFoodDataBaseProvider2 {

    private var INSTANCE: AppFoodDataBase2? = null


    fun getDatabase(context: Context): AppFoodDataBase2 {
        //val tempInstance = INSTANCE
        if (INSTANCE == null) {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppFoodDataBase2::class.java,

                    "appfood_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance

            }
        }
        return INSTANCE!!
    }

    private fun buildDataBase(context: Context):RoomDatabase
    {
        return Room.databaseBuilder(context,AppFoodDataBase2::class.java,"appfood_database").build()
    }
}