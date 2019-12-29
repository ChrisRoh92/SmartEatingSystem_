package de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.appfood_extend_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.ExtendedFood

@Database(entities = [ExtendedFood::class], version = 1)
abstract class AppFoodExtendDataBase: RoomDatabase() {

    abstract fun appFoodExtendDao(): AppFoodExtendDao
}


object AppFoodExtendDataBaseProvider {

    private var INSTANCE: AppFoodExtendDataBase? = null


    fun getDatabase(context: Context): AppFoodExtendDataBase {
        //val tempInstance = INSTANCE
        if (INSTANCE == null) {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppFoodExtendDataBase::class.java,

                    "appfood_extend_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance

            }
        }
        return INSTANCE!!
    }
}