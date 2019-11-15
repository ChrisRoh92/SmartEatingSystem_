package com.example.roomdatabaseexample.backend.databases.daily_database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Room
import de.rohnert.smeasy.backend.databases.daily_database.DailyDao


@Database(entities = [Daily::class], version = 1)
@TypeConverters(DailyConverter::class)
public abstract class DailyDataBase:RoomDatabase() {
    abstract fun dailyDao(): DailyDao
}

object DailyDataBaseProvider
{

    private var INSTANCE: DailyDataBase? = null


    fun getDatabase(context: Context): DailyDataBase {
        //val tempInstance = INSTANCE
        if (INSTANCE == null) {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DailyDataBase::class.java,

                    "daily_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance

            }
        }
        return INSTANCE!!
    }
}




