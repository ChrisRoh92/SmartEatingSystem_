package de.rohnert.smarteatingsystem.backend.databases.body_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Body::class], version = 1)
abstract class BodyDataBase: RoomDatabase() {
    abstract fun bodyDao(): BodyDao
}


object BodyDataBaseProvider
{

    private var INSTANCE: BodyDataBase? = null


    fun getDatabase(context: Context): BodyDataBase {
        //val tempInstance = INSTANCE
        if (INSTANCE == null) {
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BodyDataBase::class.java,

                    "body_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance

            }
        }
        return INSTANCE!!
    }
}