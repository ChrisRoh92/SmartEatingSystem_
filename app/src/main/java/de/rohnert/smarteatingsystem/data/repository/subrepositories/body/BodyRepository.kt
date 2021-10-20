package de.rohnert.smarteatingsystem.data.repository.subrepositories.body

import android.app.Application
import de.rohnert.smarteatingsystem.data.databases.body_database.Body
import de.rohnert.smarteatingsystem.data.databases.body_database.BodyDao
import de.rohnert.smarteatingsystem.data.databases.body_database.BodyDataBaseProvider

class BodyRepository(var application: Application)
{
    private var bodyDao: BodyDao
    private var bodyProcessor: BodyProcessor

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

    suspend fun updateBody(body: Body)
    {
        bodyDao.update(body)
    }

    suspend fun deleteBody(body: Body)
    {
        bodyDao.delete(body)
    }

    suspend fun getBodyList():ArrayList<Body>
    {
        return ArrayList(bodyDao.getBodyList())
    }

    suspend fun getBodyByDate(date:Long): Body
    {
        return bodyDao.getBodyByDate(date)
    }

    suspend fun getLatestBody():Body = bodyDao.getLatestBody()

    suspend fun getOldestBody():Body = bodyDao.getOldestBody()

    suspend fun getNumberOfBodyEntries():Int = bodyDao.getNumberOfBodyEntries()
}