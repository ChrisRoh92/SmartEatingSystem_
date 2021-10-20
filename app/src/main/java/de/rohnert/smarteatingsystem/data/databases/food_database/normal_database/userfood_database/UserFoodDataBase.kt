package de.rohnert.smarteatingsystem.data.databases.food_database.normal_database.userfood_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.rohnert.smarteatingsystem.data.databases.food_database.normal_database.Food

@Database(entities = [Food::class], version = 1)
abstract class UserFoodDataBase: RoomDatabase() {
    abstract fun userFoodDao(): UserFoodDao
}


object UserFoodDataBaseProvider
{

    private var INSTANCE: UserFoodDataBase? = null


    fun getDatabase(context: Context): UserFoodDataBase {
        //val tempInstance = INSTANCE
        if (INSTANCE == null) {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserFoodDataBase::class.java,
                    "userfood_database"
                ).build()
                INSTANCE = instance

            }
        }
        return INSTANCE!!
    }
}