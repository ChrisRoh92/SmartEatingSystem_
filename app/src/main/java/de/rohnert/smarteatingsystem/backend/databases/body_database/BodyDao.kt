package de.rohnert.smarteatingsystem.backend.databases.body_database

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

    @Query("Select * FROM bodies ORDER BY date desc ")
    suspend fun getBodyList():List<Body>

    @Query("SELECT * FROM bodies WHERE date= :date")
    suspend fun getBodyByDate(date:String): Body


}