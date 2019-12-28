package de.rohnert.smeasy.backend.databases.food_database.normal_database.favourite_foods

import android.content.Context
import androidx.room.*

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

}