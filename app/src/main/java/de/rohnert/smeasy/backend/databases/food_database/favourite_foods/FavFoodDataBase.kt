package de.rohnert.smeasy.backend.databases.food_database.favourite_foods

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import de.rohnert.smeasy.backend.databases.food_database.appfood_database.AppFoodDataBase2

@Database(entities = [FavFood::class], version = 1)
abstract class FavFoodDataBase : RoomDatabase()
{

    abstract fun favFoodDao(): FavFoodDao
}

object FavFoodDataBaseProvider {

    private var INSTANCE: FavFoodDataBase? = null


    fun getDatabase(context: Context): FavFoodDataBase {
        //val tempInstance = INSTANCE
        if (INSTANCE == null) {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavFoodDataBase::class.java,

                    "favFood"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance

            }
        }
        return INSTANCE!!
    }

    private fun buildDataBase(context: Context):RoomDatabase
    {
        return Room.databaseBuilder(context, FavFoodDataBase::class.java,"appfood_database").build()
    }
}