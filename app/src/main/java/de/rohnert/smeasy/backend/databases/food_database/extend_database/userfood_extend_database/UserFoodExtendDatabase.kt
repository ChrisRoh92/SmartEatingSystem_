package de.rohnert.smeasy.backend.databases.food_database.extend_database.userfood_extend_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.rohnert.smeasy.backend.databases.food_database.extend_database.ExtendedFood
import de.rohnert.smeasy.backend.databases.food_database.extend_database.appfood_extend_database.AppFoodExtendDao
import de.rohnert.smeasy.backend.databases.food_database.extend_database.appfood_extend_database.AppFoodExtendDataBase

@Database(entities = [ExtendedFood::class], version = 1)
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
                    .build()
                INSTANCE = instance

            }
        }
        return INSTANCE!!
    }
}