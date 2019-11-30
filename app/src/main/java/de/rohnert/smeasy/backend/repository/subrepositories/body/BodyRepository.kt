package com.example.roomdatabaseexample.backend.repository.subrepositories.body

import android.app.Application
import com.example.roomdatabaseexample.backend.databases.body_database.Body
import com.example.roomdatabaseexample.backend.databases.body_database.BodyDao
import com.example.roomdatabaseexample.backend.databases.body_database.BodyDataBaseProvider
import de.rohnert.smeasy.backend.sharedpreferences.SharedAppPreferences

class BodyRepository(var application: Application)
{
    private var bodyDao: BodyDao
    private var bodyProcessor: BodyProcessor
    private var prefs = SharedAppPreferences(application)

    init {
        var bodyDB = BodyDataBaseProvider.getDatabase(application)
        bodyDao = bodyDB.bodyDao()
        bodyProcessor = BodyProcessor()
    }

    // Methoden für die BodyDataBase

    suspend fun addNewBody(body: Body)
    {
        bodyDao.insert(body)
    }

    suspend fun updateBody(body:Body)
    {
        bodyDao.update(body)
    }

    suspend fun deleteBody(body:Body)
    {
        bodyDao.delete(body)
    }

    suspend fun getBodyList():ArrayList<Body>
    {
        return ArrayList(bodyDao.getBodyList())
    }

    suspend fun getBodyByDate(date:String):Body
    {
        return bodyDao.getBodyByDate(date)
    }
}