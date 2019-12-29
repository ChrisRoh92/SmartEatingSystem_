package de.rohnert.smarteatingsystem.backend.databases.daily_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DailyDao2
{
    @Insert
    suspend fun insert(daily: Daily)

    @Insert
    suspend fun insertAll(vararg daily: Daily)

    @Delete
    suspend fun delete(daily: Daily)

    @Update (onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(daily: Daily)

    @Query("SELECT COUNT(*) FROM dailies WHERE date= :date")
    suspend fun getNumberOfEntries(date:String):Int

    @Query("SELECT * FROM dailies")
    suspend fun getAll(): List<Daily>

    @Query("SELECT * FROM dailies")
    fun getAllLiveData(): LiveData<List<Daily>>

    @Query("SELECT * FROM dailies WHERE date= :date")
    fun getDailyByDate(date: String): LiveData<Daily>

    @Query("SELECT * FROM dailies WHERE date= :date")
    suspend fun getOffLineDailyByDate(date:String): Daily
}