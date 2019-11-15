package com.example.roomdatabaseexample.backend.databases.body_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BodyDao
{
    @Insert
    fun insert(body:Body)

    @Update
    fun update(body:Body)

    @Delete
    fun delete(body:Body)

    @Query("Select * FROM bodies")
    fun getBodyList():LiveData<List<Body>>

    @Query("SELECT * FROM bodies WHERE date= :date")
    fun getBodyByDate(date:String):Body
}