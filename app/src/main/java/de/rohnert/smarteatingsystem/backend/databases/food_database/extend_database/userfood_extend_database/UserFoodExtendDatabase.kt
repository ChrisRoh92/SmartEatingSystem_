package de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.userfood_extend_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.rohnert.smarteatingsystem.backend.databases.daily_database.DailyConverter
import de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database.ExtendedFoodConverter

@Database(entities = [ExtendedFood::class], version = 2)
@TypeConverters(ExtendedFoodConverter::class)
abstract class UserFoodExtendDatabase: RoomDatabase() {

    abstract fun userFoodExtendDao(): UserFoodExtendDao
}


object UserFoodExtendDataBaseProvider {

    private var INSTANCE: UserFoodExtendDatabase? = null


    fun getDatabase(context: Context): UserFoodExtendDatabase {
        //val tempInstance = INSTANCE
        if (INSTANCE == null) {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserFoodExtendDatabase::class.java,

                    "userfood_extend_database"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

            }
        }
        return INSTANCE!!
    }
}