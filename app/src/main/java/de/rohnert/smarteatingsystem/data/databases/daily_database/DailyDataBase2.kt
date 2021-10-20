package de.rohnert.smarteatingsystem.data.databases.daily_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Daily::class], version = 1)
@TypeConverters(DailyConverter::class)
abstract class DailyDataBase2:RoomDatabase() {

    abstract fun dailyDao2(): DailyDao2
}

    object DailyDataBaseProvider2 {

        private var INSTANCE: DailyDataBase2? = null


        fun getDatabase(context: Context): DailyDataBase2 {
            //val tempInstance = INSTANCE
            if (INSTANCE == null) {
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        DailyDataBase2::class.java,

                        "daily_database"
                    ).allowMainThreadQueries()
                        .build()
                    INSTANCE = instance

                }
            }
            return INSTANCE!!
        }

    }
