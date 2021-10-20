package de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.appfood_extend_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smarteatingsystem.data.databases.food_database.extend_database.ExtendedFoodConverter

@Database(entities = [ExtendedFood::class], version = 2)
@TypeConverters(ExtendedFoodConverter::class)
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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

            }
        }
        return INSTANCE!!
    }
}