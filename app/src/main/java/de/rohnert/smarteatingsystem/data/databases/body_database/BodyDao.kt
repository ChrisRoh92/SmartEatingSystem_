package de.rohnert.smarteatingsystem.data.databases.body_database

import androidx.room.*

@Dao
interface BodyDao
{
    @Insert
    suspend fun insert(body: Body)

    @Update
    suspend fun update(body: Body)

    @Delete
    suspend fun delete(body: Body)

    @Query("Select * FROM bodies ORDER BY date DESC")
    suspend fun getBodyList():List<Body>

    @Query("Select * FROM bodies ORDER BY date DESC Limit 1")
    suspend fun getLatestBody():Body

    @Query("Select * FROM bodies ORDER BY date ASC Limit 1")
    suspend fun getOldestBody():Body

    @Query("SELECT * FROM bodies WHERE date= :date")
    suspend fun getBodyByDate(date:Long): Body

    @Query("Select COUNT(*) FROM bodies")
    suspend fun getNumberOfBodyEntries():Int


}