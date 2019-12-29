package de.rohnert.smarteatingsystem.backend.databases.daily_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DailyDao
{
    @Insert
    fun insert(daily: Daily)

    @Insert
    fun insertAll(vararg daily: Daily)

    @Delete
    fun delete(daily: Daily)

    @Update
    fun update(daily: Daily)

    @Query("SELECT COUNT(*) FROM dailies WHERE date= :date")
    fun getNumberOfEntries(date:String):Int

    @Query("SELECT * FROM dailies")
    fun getAll(): List<Daily>

    @Query("SELECT * FROM dailies")
    fun getAllLiveData():LiveData<List<Daily>>

    @Query("SELECT * FROM dailies WHERE date= :date")
    fun getDailyByDate(date: String): LiveData<Daily>

    @Query("SELECT * FROM dailies WHERE date= :date")
    fun getOffLineDailyByDate(date:String): Daily





}